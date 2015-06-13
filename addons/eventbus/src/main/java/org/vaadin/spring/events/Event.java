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
package org.vaadin.spring.events;

import java.io.Serializable;

/**
 * A class that represents an event that has been published on an {@link org.vaadin.spring.events.EventBus}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see EventBus#publish(Object, Object)
 */
public class Event<T> implements Serializable {

    private static final long serialVersionUID = 4818820872533486223L;

    private final EventBus eventBus;

    private final Object source;

    private final String topic;
    
    private final long timestamp;

    private final T payload;

    public Event(EventBus eventBus, Object source, T payload) {
    	this(eventBus, source, payload, "");
    }

    public Event(EventBus eventBus, Object source, T payload, String topic) {
        this.eventBus = eventBus;
        this.source = source;
        this.payload = payload;
        this.topic = topic != null ? topic : "";
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets the event bus on which the event was originally published.
     *
     * @return the event bus, never {@code null}.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Gets the scope of the event.
     *
     * @return the scope, never {@code null}.
     */
    public EventScope getScope() {
        return eventBus.getScope();
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
     * Gets the string which specifies the topic of the event on the event bus.
     * 
     * @return the topic of the event, never {@code null}.
     */
    public String getTopic() {
		return topic;
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
        return String.format("%s[scope=%s, eventBus=%s, ts=%d, source=[%s], payload=[%s]]",
                getClass().getSimpleName(), getScope(), getEventBus(), getTimestamp(), getSource(), getPayload());
    }
}
