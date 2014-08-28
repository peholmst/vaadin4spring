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
 * {@link org.vaadin.spring.events.EventBusListenerMethod#filter()}
 * and stand in place of such filtering code.
 * </p>
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public interface EventBusListenerMethodFilter {

    /**
     * Criteria used to influence when an <code>@EventBusListenerMethod</code>
     * annotated method with this <code>filter</code> defined will execute
     * @param payload any Object
     * @return true if filtering condition met; false otherwise
     */
    boolean filter(Object payload);
}
