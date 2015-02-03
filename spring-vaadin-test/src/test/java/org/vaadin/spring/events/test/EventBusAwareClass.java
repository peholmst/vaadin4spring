package org.vaadin.spring.events.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.VaadinEventBusAware;

public class EventBusAwareClass implements InitializingBean, VaadinEventBusAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private EventBus eventbus;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void setVaadinEventBus(EventBus eventbus) {
        this.eventbus = eventbus;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(eventbus);
    }

    EventBus getEventbus() {
        return eventbus;
    }

    ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
