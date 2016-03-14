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

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;

/**
 * Base implementation of {@link org.vaadin.spring.events.internal.ListenerCollection.Listener} that implements
 * the {@link #supports(org.vaadin.spring.events.Event)}  method. An event is supported if:
 * <ul>
 * <li>The payload type of the listener is either the same type as, or a supertype of, the payload type of the event</li>
 * <li>The listener allows propagating events, or the event was originally published on event bus that the listener was subscribed to</li>
 * </ul>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
abstract class AbstractListenerWrapper implements ListenerCollection.Listener {

    private static final long serialVersionUID = 6211420845165980671L;

    private final EventBus owningEventBus;

    private final Object listenerTarget;

    private final boolean includingPropagatingEvents;

    private final String topic;

    public AbstractListenerWrapper(EventBus owningEventBus, Object listenerTarget, String topic, boolean includingPropagatingEvents) {
        this.owningEventBus = owningEventBus;
        this.topic = topic;
        this.listenerTarget = listenerTarget;
        this.includingPropagatingEvents = includingPropagatingEvents;
    }

    /**
     * Gets the payload type of the listener.
     */
    protected abstract Class<?> getPayloadType();

    /**
     * Gets the target object that this listener is wrapping.
     */
    public Object getListenerTarget() {
        return listenerTarget;
    }

    @Override
    public boolean supports(Event<?> event) {
        final Class<?> eventPayloadType = event.getPayload().getClass();
        return (event.getTopic().equals(topic) || topic == null) &&
                getPayloadType().isAssignableFrom(eventPayloadType) &&
                (includingPropagatingEvents || event.getEventBus().equals(owningEventBus));
    }

}
