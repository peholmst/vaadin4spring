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
package org.vaadin.spring.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A collection of {@link org.vaadin.spring.events.EventBusListener}s that uses weak references, i.e. adding
 * a listener to the collection does not prevent it from being garbage collected if it is otherwise out of scope.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
class WeakListenerCollection implements Serializable {

    private final Log logger = LogFactory.getLog(getClass());
    private final WeakHashMap<EventBusListener<?>, Class<?>> listeners = new WeakHashMap<>();

    /**
     * Adds the specified listener to the collection.
     *
     * @param listener the listener to add, never {@code null}.
     * @param <T>      the event payload type.
     */
    public <T> void add(EventBusListener<T> listener) {
        Class<?> eventPayloadType = GenericTypeResolver.resolveTypeArgument(listener.getClass(), EventBusListener.class);
        Assert.notNull(eventPayloadType, "Could not resolve payload type");
        logger.debug(String.format("Adding listener [%s] for payload type [%s]", listener, eventPayloadType.getCanonicalName()));
        synchronized (listeners) {
            listeners.put(listener, eventPayloadType);
        }
    }

    /**
     * Removes the specified listener from the collection. If the listener was not added to the collection in the first place, nothing happens.
     *
     * @param listener the listener to remove, never {@code null}.
     * @param <T>      the event payload type.
     */
    public <T> void remove(EventBusListener<T> listener) {
        logger.debug(String.format("Removing listener [%s]", listener));
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * Publishes the specified event to all listeners that can receive it.
     *
     * @param event the event to publish, never {@code null}.
     * @param <T>   the event payload type.
     */
    public <T> void publish(Event<T> event) {
        Class<T> eventPayloadType = (Class<T>) event.getPayload().getClass();
        for (EventBusListener<T> listener : getListenersForPayloadType(eventPayloadType)) {
            listener.onEvent(event);
        }
    }

    private <T> Set<EventBusListener<T>> getListenersForPayloadType(Class<T> payloadType) {
        Set<EventBusListener<T>> selectedListeners = new HashSet<>();
        synchronized (listeners) {
            for (Map.Entry<EventBusListener<?>, Class<?>> entry : listeners.entrySet()) {
                if (entry.getValue().isAssignableFrom(payloadType)) {
                    selectedListeners.add((EventBusListener<T>) entry.getKey());
                }
            }
        }
        return selectedListeners;
    }

}
