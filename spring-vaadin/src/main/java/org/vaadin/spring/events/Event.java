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

import java.io.Serializable;

/**
 * A class that represents an event that has been published on an {@link org.vaadin.spring.events.EventBus}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see EventBus#publish(Object, Object)
 */
public class Event<T> implements Serializable {

    private final EventScope scope;

    private final Object source;

    private final long timestamp;

    private final T payload;

    public Event(EventScope scope, Object source, T payload) {
        this.scope = scope;
        this.source = source;
        this.payload = payload;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets the scope of the event.
     *
     * @return the scope, never {@code null}.
     */
    public EventScope getScope() {
        return scope;
    }

    /**
     * Gets the object that published the event on the event bus.
     *
     * @return the source of the event, never {@code null}.
     */
    public Object getSource() {
        return source;
    }

    /**
     * Gets the timestamp when the event was published on the event bus.
     *
     * @return the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the payload of the event.
     *
     * @return the payload, never {@code null}.
     */
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format("%s[scope=%s, ts=%d, source=[%s], payload=[%s]]",
                getClass().getSimpleName(), getScope(), getTimestamp(), getSource(), getPayload());
    }
}
