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
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;

import javax.annotation.PreDestroy;
import java.io.Serializable;

/**
 * Implementation of {@link org.vaadin.spring.events.EventBus} that publishes events with one specific {@link org.vaadin.spring.events.EventScope}.
 * A scoped event bus can also have a parent event bus, in which case all events published on the parent bus will propagate to the scoped event bus as well.
 *
 * @author petter@vaadin.com
 */
public class ScopedEventBus implements EventBus, Serializable {

    private final Log logger = LogFactory.getLog(getClass());
    private final EventScope eventScope;

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

    /**
     * @param scope the scope of the events that this event bus handles.
     */
    public ScopedEventBus(EventScope scope) {
        this(scope, null);
    }

    /**
     * @param scope          the scope of the events that this event bus handles.
     * @param parentEventBus the parent event bus to use, may be {@code null};
     */
    public ScopedEventBus(EventScope scope, EventBus parentEventBus) {
        eventScope = scope;
        if (parentEventBus != null) {
            logger.debug("Using parent event bus [" + parentEventBus + "]");
            parentEventBus.subscribe(parentListener);
        }
        this.parentEventBus = parentEventBus;
    }

    @PreDestroy
    private void destroy() {
        if (parentEventBus != null) {
            parentEventBus.unsubscribe(parentListener);
        }
    }

    @Override
    public EventScope getScope() {
        return eventScope;
    }

    @Override
    public <T> void publish(Object sender, T payload) {
        logger.debug(String.format("Publishing payload [%s] from sender [%s] on event bus [%s]", payload, sender, this));
        listeners.publish(new Event<>(getScope(), sender, payload));
    }

    @Override
    public <T> void publish(EventScope scope, Object sender, T payload) throws UnsupportedOperationException {
        logger.debug(String.format("Trying to publish payload [%s] from sender [%s] using scope [%s] on event bus [%s]", payload, sender, scope, this));
        if (eventScope.equals(scope)) {
            publish(sender, payload);
        } else if (parentEventBus != null) {
            parentEventBus.publish(scope, sender, payload);
        } else {
            logger.warn(String.format("Could not publish payload with scope [%s] on event bus [%s]", scope, this));
            throw new UnsupportedOperationException("Could not publish event with scope " + scope);
        }
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

    @Override
    public String toString() {
        return String.format("%s[id=%d, eventScope=%s, parentEventBus=%s]", getClass().getSimpleName(), System.identityHashCode(this), eventScope, parentEventBus);
    }
}
