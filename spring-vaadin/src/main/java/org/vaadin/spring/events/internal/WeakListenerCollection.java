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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListener;

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
    private final WeakHashMap<EventBusListener<?>, ListenerMetaData> listeners = new WeakHashMap<>();

    static class ListenerMetaData implements Serializable {
        final Class<?> payloadType;
        final Object metaData;

        ListenerMetaData(Class<?> payloadType, Object metaData) {
            this.payloadType = payloadType;
            this.metaData = metaData;
        }

        boolean supportsPayload(Object payload) {
            return payloadType.isAssignableFrom(payload.getClass());
        }
    }

    public interface ListenerSpecification {
        boolean satisfies(Object metaData);
    }

    /**
     * Adds the specified listener to the collection.
     *
     * @param listener the listener to add, never {@code null}.
     * @param metaData optional meta data to associate with the listener, may be {@code null}.
     * @param <T>      the event payload type.
     */
    public <T> void add(EventBusListener<T> listener, Object metaData) {
        Class<?> eventPayloadType = GenericTypeResolver.resolveTypeArgument(listener.getClass(), EventBusListener.class);
        Assert.notNull(eventPayloadType, "Could not resolve payload type");
        logger.debug(String.format("Adding listener [%s] for payload type [%s]", listener, eventPayloadType.getCanonicalName()));
        synchronized (listeners) {
            listeners.put(listener, new ListenerMetaData(eventPayloadType, metaData));
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
     * Publishes the specified event to all listeners.
     *
     * @param event the event to publish, never {@code null}.
     * @param <T>   the event payload type.
     */
    public <T> void publish(Event<T> event) {
        publish(event, new ListenerSpecification() {
            @Override
            public boolean satisfies(Object metaData) {
                return true;
            }
        });
    }

    /**
     * Publishes the specified event to all listeners that satisfies the specification.
     *
     * @param event         the event to publish, never {@code null}.
     * @param specification the specification of which event listeners to publish to, never {@code null}.
     * @param <T>           the event payload type.
     */
    public <T> void publish(Event<T> event, ListenerSpecification specification) {
        for (EventBusListener<T> listener : getListenersForPayload(event.getPayload(), specification)) {
            listener.onEvent(event);
        }
    }

    private <T> Set<EventBusListener<T>> getListenersForPayload(T payload, ListenerSpecification specification) {
        Set<EventBusListener<T>> selectedListeners = new HashSet<>();
        synchronized (listeners) {
            for (Map.Entry<EventBusListener<?>, ListenerMetaData> entry : listeners.entrySet()) {
                if (entry.getValue().supportsPayload(payload) && specification.satisfies(entry.getValue().metaData)) {
                    selectedListeners.add((EventBusListener<T>) entry.getKey());
                }
            }
        }
        return selectedListeners;
    }

}
