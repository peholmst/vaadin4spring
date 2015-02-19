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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.vaadin.spring.events.*;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.internal.ClassUtils;

import javax.annotation.PreDestroy;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Implementation of {@link org.vaadin.spring.events.EventBus} that publishes events with one specific {@link org.vaadin.spring.events.EventScope}.
 * A scoped event bus can also have a parent event bus, in which case all events published on the parent bus will propagate to the scoped event bus as well.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class ScopedEventBus implements EventBus, Serializable {

    private static final long serialVersionUID = 1637290543180920954L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final EventScope eventScope;

    private final ListenerCollection listeners = new ListenerCollection();

    private EventBus parentEventBus;
    private EventBusListener<Object> parentListener = new EventBusListener<Object>() {
        
        private static final long serialVersionUID = -8276470908536582989L;

        @Override
        public void onEvent(final Event<Object> event) {
            logger.debug("Propagating event [{}] from parent event bus [{}] to event bus [{}]", event, parentEventBus, ScopedEventBus.this);
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
        this.parentEventBus = parentEventBus;
        if (parentEventBus != null) {
            if (AopUtils.isJdkDynamicProxy(parentEventBus)) {
                logger.debug("Parent event bus [{}] is proxied, trying to get the real EventBus instance", parentEventBus);
                try {
                    this.parentEventBus = (EventBus) ((Advised) parentEventBus).getTargetSource().getTarget();
                } catch (Exception e) {
                    logger.error("Could not get target EventBus from proxy", e);
                    throw new RuntimeException("Could not get parent event bus", e);
                }
            }
            logger.debug("Using parent event bus [{}]", this.parentEventBus);
            this.parentEventBus.subscribe(parentListener);
        }
    }

    @PreDestroy
    void destroy() {
        logger.trace("Destroying event bus [{}] and removing all listeners", this);
        listeners.clear();
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
        logger.debug("Publishing payload [{}] from sender [{}] on event bus [{}]", payload, sender, this);
        listeners.publish(new Event<T>(this, sender, payload));
    }

    @Override
    public <T> void publish(EventScope scope, Object sender, T payload) throws UnsupportedOperationException {
        logger.debug("Trying to publish payload [{}] from sender [{}] using scope [{}] on event bus [{}]", payload, sender, scope, this);
        if (eventScope.equals(scope)) {
            publish(sender, payload);
        } else if (parentEventBus != null) {
            parentEventBus.publish(scope, sender, payload);
        } else {
            logger.warn("Could not publish payload with scope [{}] on event bus [{}]", scope, this);
            throw new UnsupportedOperationException("Could not publish event with scope " + scope);
        }
    }

    @Override
    public <T> void subscribe(EventBusListener<T> listener) {
        subscribe(listener, true);
    }

    @Override
    public <T> void subscribe(EventBusListener<T> listener, boolean includingPropagatingEvents) {
        logger.trace("Subscribing listener [{}] to event bus [{}], includingPropagatingEvents = {}", listener, this, includingPropagatingEvents);
        listeners.add(new EventBusListenerWrapper(this, listener, includingPropagatingEvents));
    }

    @Override
    public void subscribe(Object listener) {
        subscribe(listener, true);
    }

    @Override
    public void subscribe(final Object listener, final boolean includingPropagatingEvents) {
        logger.trace("Subscribing listener [{}] to event bus [{}], includingPropagatingEvents = {}", listener, this, includingPropagatingEvents);

        final int[] foundMethods = new int[1];
        ClassUtils.visitClassHierarchy(new ClassUtils.ClassVisitor() {
            @Override
            public void visit(Class<?> clazz) {
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(EventBusListenerMethod.class)) {
                        if (m.getParameterTypes().length == 1) {
                            logger.trace("Found listener method [{}] in listener [{}]", m.getName(), listener);
                            MethodListenerWrapper l = new MethodListenerWrapper(ScopedEventBus.this, listener, includingPropagatingEvents, m);
                            listeners.add(l);
                            foundMethods[0]++;
                        } else {
                            throw new IllegalArgumentException("Listener method " + m.getName() + " does not have the required signature");
                        }
                    }
                }
            }
        }, listener.getClass());

        if (foundMethods[0] == 0) {
            logger.warn("Listener [{}] did not contain a single listener method!", listener);
        }
    }

    @Override
    public <T> void unsubscribe(EventBusListener<T> listener) {
        unsubscribe((Object) listener);
    }

    @Override
    public void unsubscribe(final Object listener) {
        logger.trace("Unsubscribing listener [{}] from event bus [{}]", listener, this);
        listeners.removeAll(new ListenerCollection.ListenerFilter() {
            @Override
            public boolean passes(ListenerCollection.Listener l) {
                return (l instanceof AbstractListenerWrapper) && (((AbstractListenerWrapper) l).getListenerTarget() == listener);
            }
        });
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
        return String.format("%s[id=%x, eventScope=%s, parentEventBus=%s]", getClass().getSimpleName(), System.identityHashCode(this), eventScope, parentEventBus);
    }

    /**
     * Default implementation of {@link org.vaadin.spring.events.EventBus.ApplicationEventBus}.
     */
    public static class DefaultApplicationEventBus extends ScopedEventBus implements ApplicationEventBus {

        public DefaultApplicationEventBus() {
            super(EventScope.APPLICATION);
        }
    }

    /**
     * Default implementation of {@link org.vaadin.spring.events.EventBus.SessionEventBus}.
     */
    public static class DefaultSessionEventBus extends ScopedEventBus implements SessionEventBus {

        public DefaultSessionEventBus(ApplicationEventBus parentEventBus) {
            super(EventScope.SESSION, parentEventBus);
        }
    }

    /**
     * Default implementation of {@link org.vaadin.spring.events.EventBus.UIEventBus}.
     */
    public static class DefaultUIEventBus extends ScopedEventBus implements UIEventBus {

        public DefaultUIEventBus(SessionEventBus parentEventBus) {
            super(EventScope.UI, parentEventBus);
        }
    }

    /**
     * Default implementation of {@link org.vaadin.spring.events.EventBus.ViewEventBus}.
     */
    public static class DefualtViewEventBus extends ScopedEventBus implements ViewEventBus {

        public DefualtViewEventBus(UIEventBus parentEventBus) {
            super(EventScope.VIEW, parentEventBus);
        }
    }
}
