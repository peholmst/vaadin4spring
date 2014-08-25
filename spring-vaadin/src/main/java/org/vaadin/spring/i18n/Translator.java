/*
 * Copyright 2014 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.i18n;

import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.vaadin.spring.internal.ClassUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.vaadin.spring.internal.ClassUtils.visitClassHierarchy;

/**
 * This translator class has been designed with Vaadin UIs in mind, but it works with other classes as well. The idea is that
 * a UI component is composed of other components (e.g. labels and text fields) that contain properties that need to be translated (e.g. captions, descriptions).
 * These translatable properties can be mapped to {@link org.springframework.context.MessageSource} keys by using the {@link org.vaadin.spring.i18n.TranslatedProperty} annotation.
 * A {@code Translator} instance would then be created for the UI component (the target object). When the {@link #translate(java.util.Locale, org.springframework.context.MessageSource)}  method
 * is invoked, the translator will go through all fields and getter methods that are annotated and update the translated properties with values from the {@code MessageSource}.
 * <p>
 * For example, a label with a translatable value would be defined like this:
 * <pre>
 * &#64;TranslatedProperty(property = "value", key = "myLabel.value")
 * private Label myLabel;
 * </pre>
 * When translating the UI, the translator would first get the message with the key {@code myLabel.value} from the {@code MessageSource}, and then
 * invoke {@link com.vaadin.ui.Label#setValue(String)}, passing in the message as the only parameter.
 * <p>
 * The UI component is itself responsible for creating the translator and invoking the {@link #translate(java.util.Locale, org.springframework.context.MessageSource)} method
 * when necessary (normally on initial component setup and when the locale changes).
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.i18n.TranslatedProperties
 * @see org.vaadin.spring.i18n.TranslatedProperty
 */
public class Translator implements Serializable {

    private final Object target;

    private transient Map<TranslatedProperty, Field> translatedFields;
    private transient Map<TranslatedProperty, Method> translatedMethods;

    /**
     * Creates a new translator.
     *
     * @param target the object that will be translated, never {@code null}.
     */
    public Translator(Object target) {
        this.target = target;
        analyzeTargetClass();
    }

    private void readObject(ObjectInputStream io) throws IOException, ClassNotFoundException {
        io.defaultReadObject();
        analyzeTargetClass();
    }

    private void analyzeTargetClass() {
        translatedFields = new HashMap<>();
        translatedMethods = new HashMap<>();
        visitClassHierarchy(new ClassUtils.ClassVisitor() {
            @Override
            public void visit(Class<?> clazz) {
                analyzeFields(clazz);
                analyzeMethods(clazz);
            }
        }, target.getClass());
    }

    private void analyzeMethods(Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != Void.TYPE) {
                if (m.isAnnotationPresent(TranslatedProperty.class)) {
                    translatedMethods.put(m.getAnnotation(TranslatedProperty.class), m);
                } else if (m.isAnnotationPresent(TranslatedProperties.class)) {
                    for (TranslatedProperty annotation : m.getAnnotation(TranslatedProperties.class).value()) {
                        translatedMethods.put(annotation, m);
                    }
                }
            }
        }
    }

    private void analyzeFields(Class<?> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(TranslatedProperty.class)) {
                translatedFields.put(f.getAnnotation(TranslatedProperty.class), f);
            } else if (f.isAnnotationPresent(TranslatedProperties.class)) {
                for (TranslatedProperty annotation : f.getAnnotation(TranslatedProperties.class).value()) {
                    translatedFields.put(annotation, f);
                }
            }
        }
    }

    /**
     * Translates the target object using the specified locale and message source.
     *
     * @param locale        the locale to use when fetching messages, never {@code null}.
     * @param messageSource the message source to fetch messages from, never {@code null}.
     */
    public void translate(Locale locale, MessageSource messageSource) {
        translateFields(locale, messageSource);
        translatedMethods(locale, messageSource);
    }

    private void translateFields(Locale locale, MessageSource messageSource) {
        for (Map.Entry<TranslatedProperty, Field> fieldEntry : translatedFields.entrySet()) {
            final Field field = fieldEntry.getValue();
            final TranslatedProperty annotation = fieldEntry.getKey();
            field.setAccessible(true);
            Object fieldValue;
            try {
                fieldValue = field.get(target);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + field.getName());
            }
            if (fieldValue != null) {
                setPropertyValue(fieldValue, annotation.property(),
                        messageSource.getMessage(annotation.key(), null, annotation.defaultValue(), locale));
            }
        }
    }

    private void translatedMethods(Locale locale, MessageSource messageSource) {
        for (Map.Entry<TranslatedProperty, Method> methodEntry : translatedMethods.entrySet()) {
            final Method method = methodEntry.getValue();
            final TranslatedProperty annotation = methodEntry.getKey();
            method.setAccessible(true);
            Object methodValue;
            try {
                methodValue = method.invoke(target);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access method " + method.getName());
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Could not invoke method " + method.getName(), e);
            }
            if (methodValue != null) {
                setPropertyValue(methodValue, annotation.property(),
                        messageSource.getMessage(annotation.key(), null, annotation.defaultValue(), locale));
            }
        }
    }

    private static void setPropertyValue(Object target, String propertyName, String value) {
        final String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        try {
            final Method setterMethod = target.getClass().getMethod(setterMethodName, String.class);
            setterMethod.invoke(target, value);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No public setter method found for property " + propertyName);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Could not invoke setter method for property " + propertyName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access setter method for property " + propertyName);
        }
    }
}
