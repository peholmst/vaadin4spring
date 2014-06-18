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
 * Interface defining an event bus. This event bus infrastructure complements the {@link org.springframework.context.ApplicationEventPublisher} in the following ways:
 * <ul>
 * <li>Events propagate from parent buses to children</li>
 * <li>Events are scoped</li>
 * </ul>
 * <p/>
 * There are three event scopes, and therefore three event bus types:
 * <ol>
 * <li>{@link EventScope#APPLICATION} events are published to the entire application.</li>
 * <li>{@link EventScope#SESSION} events are published to the current session.</li>
 * <li>{@link EventScope#UI} events are published to the current UI.</li>
 * </ol>
 * <p/>
 * The event buses are chained in the following way:
 * <ul>
 * <li>Application events are propagated to the session event bus.</li>
 * <li>Session events are propagated to the UI event bus.</li>
 * </ul>
 * Furthermore, {@link org.springframework.context.ApplicationEventPublisher} events are propagated to the application event bus (and further down the chain to the UI event bus).
 * <p/>
 * The primary {@code EventBus} implementation is always UI-scoped and can be injected into your UI classes like this:
 * <code>
 * &#64;Autowired EventBus myUIScopedEventBus;
 * </code>
 * With this implementation, you can subscribe to and publish events of all scopes (see {@link #publish(EventScope, Object, Object)}).
 * <p/>
 * However, if you want to inject any of the other
 * event buses, you have to use the {@link org.vaadin.spring.events.EventBusScope} qualifier. For example, the following code will inject the session event bus:
 * <code>
 * &#64;Autowired &#64;EventBusScope(EventScope.SESSION) EventBus myUIScopedEventBus;
 * </code>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface EventBus {

    /**
     * Publishes the specified payload on the event bus, using the scope of this particular event bus.
     *
     * @param sender  the object that published the event, never {@code null}.
     * @param payload the payload of the event to publish, never {@code null}.
     * @param <T>     the type of the payload.
     * @see #getScope()
     */
    <T> void publish(Object sender, T payload);

    /**
     * Publishes the specified payload on the event bus, or any of its parent buses, depending on the event scope.
     *
     * @param scope   the scope of the event, never {@code null}.
     * @param sender  the object that published the event, never {@code null}.
     * @param payload the payload of the event to publish, never {@code null}.
     * @param <T>     the type of the payload;
     * @throws UnsupportedOperationException if the payload could not be published with the specified scope.
     * @see #publish(Object, Object)
     */
    <T> void publish(EventScope scope, Object sender, T payload) throws UnsupportedOperationException;

    /**
     * Gets the scope of the events published on this event bus.
     *
     * @return the event scope, never {@code null}.
     * @see org.vaadin.spring.events.Event#getScope()
     */
    EventScope getScope();

    /**
     * Subscribes the specified listener to the event bus, including propagated events from parent event buses.
     * The event bus will analyse the payload type of the listener to determine which events it is interested in receiving.
     * This is the same as calling {@link #subscribe(EventBusListener, boolean) subscribe(listener, true)}.
     *
     * @param listener the listener to subscribe, never {@code null}.
     * @param <T>      the type of payload the listener is interested in.
     * @see #unsubscribe(EventBusListener)
     */
    <T> void subscribe(EventBusListener<T> listener);

    /**
     * Subscribes the specified listener to the event bus. The event bus will analyse the
     * payload type of the listener to determine which events it is interested in receiving.
     *
     * @param listener                   the listener to subscribe, never {@code null}.
     * @param includingPropagatingEvents true to notify the listener of events that have propagated from the chain of parent event buses, false to only notify the listeners of events that are directly published on this event bus.
     * @param <T>                        the type of payload the listener is interested in.
     * @see #unsubscribe(EventBusListener)
     */
    <T> void subscribe(EventBusListener<T> listener, boolean includingPropagatingEvents);

    /**
     * Subscribes the specified listener to the event bus. The listener need not implement the {@link org.vaadin.spring.events.EventBusListener} interface,
     * but must contain one or more methods that are annotated with the {@link org.vaadin.spring.events.EventBusListenerMethod} interface and conform to one of these method
     * signatures: <code>myMethodName(Event&lt;MyPayloadType&gt;)</code> or <code>myMethodName(MyPayloadType)</code>. The event bus will analyse the payload type of the listener methods to determine
     * which events the different methods are interested in receiving. This is the same as calling {@link #subscribe(Object, boolean) subscribe(listener, true)}.
     *
     * @param listener the listener to subscribe, never {@code null}.
     */
    void subscribe(Object listener);

    /**
     * Subscribes the specified listener to the event bus. The listener need not implement the {@link org.vaadin.spring.events.EventBusListener} interface,
     * but must contain one or more methods that are annotated with the {@link org.vaadin.spring.events.EventBusListenerMethod} interface and conform to one of these method
     * signatures: <code>myMethodName(Event&lt;MyPayloadType&gt;)</code> or <code>myMethodName(MyPayloadType)</code>. The event bus will analyse the payload type of the listener methods to determine
     * which events the different methods are interested in receiving.
     *
     * @param listener                   the listener to subscribe, never {@code null}.
     * @param includingPropagatingEvents true to notify the listener of events that have propagated from the chain of parent event buses, false to only notify the listeners of events that are directly published on this event bus.
     * @see #unsubscribe(Object)
     */
    void subscribe(Object listener, boolean includingPropagatingEvents);

    /**
     * Unsubscribes the specified listener from the event bus.
     *
     * @param listener the listener to unsubscribe, never {@code null}.
     * @param <T>      the type of the payload.
     * @see #subscribe(EventBusListener)
     * @see #subscribe(EventBusListener, boolean)
     */
    <T> void unsubscribe(EventBusListener<T> listener);

    /**
     * Unsubscribes the specified listener (and all its listener methods) from the event bus.
     *
     * @param listener the listener to unsubscribe, never {@code null}.
     * @see #subscribe(Object)
     * @see #subscribe(Object, boolean)
     */
    void unsubscribe(Object listener);

}
