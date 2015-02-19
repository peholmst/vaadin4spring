package org.vaadin.spring.events.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusAware;

public class EventBusAwareClass implements InitializingBean, EventBusAware.ApplicationEventBusAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private EventBus.ApplicationEventBus eventBus;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(eventBus);
    }

    EventBus getEventBus() {
        return eventBus;
    }

    ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationEventBus(EventBus.ApplicationEventBus applicationEventBus) {
        this.eventBus = applicationEventBus;
    }
}
