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

import java.io.Serializable;

/**
 * Base class for an {@link org.vaadin.spring.events.EventBus} that publishes events with one specific {@link org.vaadin.spring.events.EventScope}.
 * A scoped event bus can also have a parent event bus, in which case all events published on the parent bus will propagate to the scoped event bus as well.
 *
 * @author petter@vaadin.com
 */
public abstract class ScopedEventBus implements EventBus, Serializable {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * A list of listeners subscribed to this event bus.
     */
    protected final WeakListenerCollection listeners = new WeakListenerCollection();

    private EventBus parentEventBus;
    private EventBusListener<Object> parentListener = new EventBusListener<Object>() {
        @Override
        public void onEvent(Event<Object> event) {
            logger.debug(String.format("Propagating event [%s] from parent event bus [%s] to event bus [%s]", event, parentEventBus, ScopedEventBus.this));
            listeners.publish(event);
        }
    };

    protected ScopedEventBus() {
        this(null);
    }

    /**
     * @param parentEventBus the parent event bus to use, may be {@code null};
     */
    protected ScopedEventBus(EventBus parentEventBus) {
        if (parentEventBus != null) {
            logger.debug("Using parent event bus [" + parentEventBus + "]");
            parentEventBus.subscribe(parentListener);
        }
        this.parentEventBus = parentEventBus;
    }

    /**
     * Gets the scope of the events published on this event bus.
     *
     * @return the event scope, never {@code null}.
     * @see org.vaadin.spring.events.Event#getScope()
     */
    protected abstract EventScope getScope();

    @Override
    public <T> void publish(Object sender, T payload) {
        logger.debug(String.format("Publishing payload [%s] from sender [%s] on event bus [%s]", payload, sender, this));
        listeners.publish(new Event<T>(getScope(), this, sender, payload));
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation uses weak references to store the listeners, so make sure you store a reference to the listener
     * somewhere else until you no longer need it.
     */
    @Override
    public <T> void subscribe(EventBusListener<T> listener) {
        logger.debug(String.format("Subscribing listener [%s] to event bus [%s]", listener, this));
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation uses weak references to store listeners, which means that the listener will be automatically removed
     * once it gets garbage collected.
     */
    @Override
    public <T> void unsubscribe(EventBusListener<T> listener) {
        logger.debug(String.format("Unsubscribing listener [%s] from event bus [%s]", listener, this));
        listeners.remove(listener);
    }

    /**
     * Gets the parent of this event bus. Events published on the parent bus will also
     * propagate to the listeners of this event bus.
     *
     * @return the parent event bus, or {@code null} if this event bus has no parent.
     */
    protected EventBus getParentEventBus() {
        return parentEventBus;
    }
}
