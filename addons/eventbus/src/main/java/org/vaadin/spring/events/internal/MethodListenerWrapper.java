/*
 * Copyright 2015 The original authors
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

import org.vaadin.spring.events.*;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Implementation of {@link org.vaadin.spring.events.internal.AbstractListenerWrapper} that wraps an object
 * that contains a method annotated with {@link org.vaadin.spring.events.annotation.EventBusListenerMethod}. If the object
 * contains multiple listener methods, multiple instances of this class should be created.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
class MethodListenerWrapper extends AbstractListenerWrapper {

    private static final long serialVersionUID = -3624543380547361337L;
    private final Class<?> payloadType;
    private final boolean payloadMethod;
    private transient Method listenerMethod;
    private final String topic;

    public MethodListenerWrapper(EventBus owningEventBus, Object listenerTarget, String topic, boolean includingPropagatingEvents, Method listenerMethod) {
        super(owningEventBus, listenerTarget, topic, includingPropagatingEvents);
        this.topic = topic;
        if (listenerMethod.getParameterTypes()[0] == Event.class) {
            ParameterizedType type = (ParameterizedType) listenerMethod.getGenericParameterTypes()[0];
            payloadType = (Class<?>) type.getActualTypeArguments()[0];
            payloadMethod = false;
        } else {
            payloadType = listenerMethod.getParameterTypes()[0];
            payloadMethod = true;
        }
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
            if (payloadMethod) {
                listenerMethod.invoke(getListenerTarget(), event.getPayload());
            } else {
                listenerMethod.invoke(getListenerTarget(), event);
            }
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

    @Override
    public boolean supports(Event<?> event) {
        boolean supports = super.supports(event);
        try {
            if (listenerMethod.isAnnotationPresent(EventBusListenerMethod.class)) {
                supports = supports && isInterestedListenerMethod(event);
            }

            if (topic != null) {
                return supports;
            }

            if (listenerMethod.isAnnotationPresent(EventBusListenerTopic.class) && supports) {
                supports = isInTopic(event);
            } else if (!event.getTopic().isEmpty()) {
                supports = false;
            }
        } catch (Exception e) {
            throw new RuntimeException("A checked exception occurred while invoking listener method " + listenerMethod.getName(), e);
        }
        return supports;
    }

    private boolean isInterestedListenerMethod(Event<?> event) throws InstantiationException, IllegalAccessException {
        EventBusListenerMethod annotation = listenerMethod.getAnnotation(EventBusListenerMethod.class);
        EventBusListenerMethodFilter filter = annotation.filter().newInstance();
        EventScope scope = annotation.scope();
        if (scope.equals(EventScope.UNDEFINED)) {
            scope = event.getScope();
        }
        return filter.filter(event)
                && event.getScope().equals(scope) && isFromSource(event, annotation.source());
    }

    private boolean isFromSource(Event<?> event, Class<?>[] sources) {
        if (sources.length == 0) {
            return true;
        }

        boolean result = false;

        for (int i = 0; i < sources.length && !result; i++) {
            result |= sources[i].isAssignableFrom(event.getSource().getClass());
        }

        return result;
    }

    private boolean isInTopic(Event<?> event) throws InstantiationException, IllegalAccessException {
        EventBusListenerTopic annotation = listenerMethod.getAnnotation(EventBusListenerTopic.class);
        TopicFilter filter = annotation.filter().newInstance();
        return filter.validTopic(event.getTopic(), annotation.topic());
    }

}
