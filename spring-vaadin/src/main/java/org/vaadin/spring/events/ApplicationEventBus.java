package org.vaadin.spring.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

/**
 * An event bus for {@link org.vaadin.spring.events.EventScope#APPLICATION} scoped events. Any events published using
 * the {@link org.springframework.context.ApplicationContext#publishEvent(org.springframework.context.ApplicationEvent)} method
 * will also be published on this bus. However, any events published on this bus directly will NOT be propagated to the Spring application context.
 *
 * @author petter@vaadin.com
 */
@Scope("singleton")
public class ApplicationEventBus extends ScopedEventBus implements ApplicationListener<ApplicationEvent> {

    @Override
    protected EventScope getScope() {
        return EventScope.APPLICATION;
    }

    @Override
    protected EventBus getParentEventBus() {
        return null;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        publish(event.getSource(), event);
    }
}
