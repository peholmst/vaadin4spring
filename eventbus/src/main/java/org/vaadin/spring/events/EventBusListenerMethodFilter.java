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

/**
 * <p>
 * A method annotated with <code>@EventBusListenerMethod</code> will be invoked if it is
 * subscribed to the <code>EventBus</code> that published the <code>Event</code>
 * (or <code>Object</code> payload).  That method will oftentimes employ filtering code
 * in the method internals, because of the possibility that multiple annotated methods
 * might listen to same the <code>Event</code>.
 * </p>
 * <p>
 * As a convenience, an implementation of this filter may be defined in
 * {@link org.vaadin.spring.events.annotation.EventBusListenerMethod#filter()}
 * and stand in place of such filtering code.
 * </p>
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public interface EventBusListenerMethodFilter {

    /**
     * Criteria used to influence when an <code>@EventBusListenerMethod</code>
     * annotated method with this <code>filter</code> defined will execute
     * @param event EventBus event
     * @return true if filtering condition met; false otherwise
     */
    boolean filter(Event<?> event);
}
