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
package org.vaadin.spring.events.internal;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Implementation of {@link org.vaadin.spring.events.internal.AbstractListenerWrapper} that wraps an object
 * that contains a method annotated with {@link org.vaadin.spring.events.EventBusListenerMethod}. If the object
 * contains multiple listener methods, multiple instances of this class should be created.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
class MethodListenerWrapper extends AbstractListenerWrapper {

    private final Class<?> payloadType;
    private transient Method listenerMethod;

    public MethodListenerWrapper(EventBus owningEventBus, Object listenerTarget, boolean includingPropagatingEvents, Method listenerMethod) {
        super(owningEventBus, listenerTarget, includingPropagatingEvents);
        ParameterizedType type = (ParameterizedType) listenerMethod.getGenericParameterTypes()[0];
        payloadType = (Class<?>) type.getActualTypeArguments()[0];
        this.listenerMethod = listenerMethod;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // TODO Read listener method info and look up method
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        // TODO Write listener method info
    }

    @Override
    public Class<?> getPayloadType() {
        return payloadType;
    }

    @Override
    public void publish(Event<?> event) {
        listenerMethod.setAccessible(true);
        try {
            listenerMethod.invoke(getListenerTarget(), event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access listener method " + listenerMethod.getName());
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            } else {
                throw new RuntimeException("A checked exception occurred while invoking listener method " + listenerMethod.getName(), targetException);
            }
        }
    }
}
