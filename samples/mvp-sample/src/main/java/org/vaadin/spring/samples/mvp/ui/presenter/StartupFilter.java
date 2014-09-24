package org.vaadin.spring.samples.mvp.ui.presenter;
import org.vaadin.spring.events.EventBusListenerMethodFilter;

/**
 * A filter for startup events.
 * Filters methods annotated with {@link EventBusListenerMethodFilter}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class StartupFilter implements EventBusListenerMethodFilter {

    @Override
    public boolean filter(Object payload) {
        boolean result = false;
        if (Action.class.isAssignableFrom(payload.getClass())) {
            Action action = (Action) payload;
            if (action.equals(Action.START)) {
                result = true;
            }
        }
        return result;
    }

}
