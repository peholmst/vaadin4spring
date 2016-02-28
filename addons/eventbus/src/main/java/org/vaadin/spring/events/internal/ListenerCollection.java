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

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.Event;

/**
 * A collection of listeners. Intended only for internal use by the framework.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
class ListenerCollection implements Serializable {

    private static final long serialVersionUID = -6237902400879667320L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Set<Listener> listeners = new HashSet<Listener>();
    private final Set<Listener> weakListeners = Collections.newSetFromMap(new WeakHashMap<Listener, Boolean>());

    /**
     * Interface defining a listener.
     */
    public interface Listener extends Serializable {

        /**
         * Checks if this listener supports the specified event.
         *
         * @param event the event to check, never {@code null}.
         * @return true if the event is supported, false otherwise.
         */
        boolean supports(Event<?> event);

        /**
         * Publishes the event to the listener.
         *
         * @param event the event to publish, never {@code null}.
         */
        void publish(Event<?> event);
    }

    /**
     * Interface defining a listener filter.
     */
    public interface ListenerFilter {
        /**
         * Checks if the specified listener passes the filter.
         *
         * @param listener the listener to check, never {@code null}.
         * @return true if the listener passes the filter, false otherwise.
         */
        boolean passes(Listener listener);
    }

    /**
     * Adds the specified {@link org.vaadin.spring.events.internal.ListenerCollection.Listener} to the listener
     * collection.
     *
     * @param listener the listener to add, never {@code null}.
     * @see #remove(org.vaadin.spring.events.internal.ListenerCollection.Listener)
     */
    public void add(Listener listener) {
        logger.trace("Adding listener [{}]", listener);
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Adds the specified {@link org.vaadin.spring.events.internal.ListenerCollection.Listener} to the listener
     * collection,
     * using a weak reference. This means the listener does not need to be removed to be eligible for garbage
     * collection.
     *
     * @param listener the listener to add, never {@code null}.
     */
    public void addWithWeakReference(Listener listener) {
        logger.trace("Adding listener [{}] using a weak reference", listener);
        synchronized (weakListeners) {
            weakListeners.add(listener);
        }
    }

    /**
     * Removes a {@link org.vaadin.spring.events.internal.ListenerCollection.Listener} previously added by 
     * {@link #add(org.vaadin.spring.events.internal.ListenerCollection.Listener)}.
     * If no listener definition is found in the collection, nothing happens.
     *
     * @param listener the listener to remove, never {@code null}.
     * @see #add(org.vaadin.spring.events.internal.ListenerCollection.Listener)
     */
    public void remove(Listener listener) {
        logger.trace("Removing listener [{}]", listener);
        synchronized (listeners) {
            listeners.remove(listener);
        }
        synchronized (weakListeners) {
            weakListeners.remove(listener);
        }
    }

    /**
     * Removes all {@link org.vaadin.spring.events.internal.ListenerCollection.Listener}s that pass the specified filter
     * and that were previously added by
     * {@link #add(org.vaadin.spring.events.internal.ListenerCollection.Listener)}.
     *
     * @param filter the filter that specifies which listeners to remove, never {@code null}.
     */
    public void removeAll(ListenerFilter filter) {
        synchronized (listeners) {
            removeFilteredListenersFromSet(filter, listeners);
        }
        synchronized (weakListeners) {
            removeFilteredListenersFromSet(filter, weakListeners);
        }
    }

    /**
     * Removes all {@link org.vaadin.spring.events.internal.ListenerCollection.Listener}s from the collection.
     */
    public void clear() {
        synchronized (listeners) {
            listeners.clear();
        }
        synchronized (weakListeners) {
            weakListeners.clear();
        }
    }

    /**
     * Publishes the specified {@code event} to all
     * {@link org.vaadin.spring.events.internal.ListenerCollection.Listener}s that support it.
     *
     * @param event the event to publish, never {@code null}.
     * @see org.vaadin.spring.events.internal.ListenerCollection.Listener#publish(org.vaadin.spring.events.Event)
     * @see org.vaadin.spring.events.internal.ListenerCollection.Listener#supports(org.vaadin.spring.events.Event)
     */
    public void publish(Event<?> event) {
        Set<Listener> interestedListeners = new HashSet<Listener>();
        synchronized (listeners) {
            addSupportedListenersToSet(listeners, interestedListeners, event);
        }
        synchronized (weakListeners) {
            addSupportedListenersToSet(weakListeners, interestedListeners, event);
        }
        if (interestedListeners.isEmpty()) {
            logger.debug("No listeners supported event [{}]", event);
        } else {
            for (Listener listener : interestedListeners) {
                logger.trace("Publishing event [{}] to listener [{}]", event, listener);
                listener.publish(event);
            }
        }
    }

    private <T> void addSupportedListenersToSet(Set<Listener> candidateListeners, Set<Listener> selectedListeners,
        Event<T> event) {
        for (Listener candidateListener : candidateListeners) {
            if (candidateListener.supports(event)) {
                logger.trace("Listener [{}] supports event [{}]", candidateListener, event);
                selectedListeners.add(candidateListener);
            }
        }
    }

    private void removeFilteredListenersFromSet(ListenerFilter filter, Set<Listener> listenerSet) {
        for (Iterator<Listener> it = listenerSet.iterator(); it.hasNext();) {
            Listener listener = it.next();
            if (filter.passes(listener)) {
                logger.trace("Removing listener [{}]", listener);
                it.remove();
            }
        }
    }
}
