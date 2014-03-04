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

/**
 * Interface defining an event bus.
 * TODO Explain the relationship with the default Spring event bus
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface EventBus {

    /**
     * Publishes the specified payload on the event bus.
     *
     * @param sender  the object that published the event, never {@code null}.
     * @param payload the payload of the event to publish, never {@code null}.
     * @param <T>     the type of the payload.
     */
    <T> void publish(Object sender, T payload);

    /**
     * Subscribes the specified listener to the event bus. The event bus will analyse the
     * payload type of the listener to determine which events it is interested in receiving.
     *
     * @param listener the listener to subscribe, never {@code null}.
     * @param <T>      the type of payload the listener is interested in.
     */
    <T> void subscribe(EventBusListener<T> listener);

    /**
     * Unsubscribes the specified listener from the event bus.
     *
     * @param listener the listener to unsubscribe, never {@code null}.
     * @param <T>      the type of the payload.
     */
    <T> void unsubscribe(EventBusListener<T> listener);

}
