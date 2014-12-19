package org.vaadin.spring.boot.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.vaadin.spring.boot.VaadinAutoConfiguration;
import org.vaadin.spring.events.EventBus;

public class VaadinApplicationContext implements ApplicationContextAware, InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(VaadinAutoConfiguration.class);
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(context, "Failed to autowire <ApplicationContext>");
		logger.debug("{} initialized", getClass().getName());	
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static EventBus getEventBus() {
		return context.getBean(EventBus.class);
	}
}
