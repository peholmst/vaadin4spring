package org.vaadin.spring.events;


/**
 * A default filter implementation which always returns true.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 * @see org.vaadin.spring.events.EventBusListenerMethod#scope()
 */
class NoEventBusListenerMethodFilter implements EventBusListenerMethodFilter {

    @Override
    public boolean filter(Object payload) {
        return true;
    }

}
