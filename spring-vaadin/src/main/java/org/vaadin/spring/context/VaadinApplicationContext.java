package org.vaadin.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vaadin.spring.events.EventBus;

/**
 * VaadinApplicationContext allows static access to the {@link org.springframework.context.ApplicationContext}.
 * This implementation exists to provide access from non-managed spring beans.
 * 
 * <p>An VaadinApplicationContext provides:
 * <ul>
 * <li>Access to the Spring {@link org.springframework.context.ApplicationContext}.
 * <li>Access to the Vaadin {@link org.vaadin.spring.events.EventBus}
 * </ul> * 
 * 
 * @author G.J.R. Timmer
 * @see org.springframework.context.ApplicationContext
 * @see org.vaadin.spring.events.EventBus
 */
public class VaadinApplicationContext implements InitializingBean, ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(VaadinApplicationContext.class);
	
	private static ApplicationContext context;
	
	/**
     * Return the spring {@link org.springframework.context.ApplicationContext}
     * @return the spring {@link org.springframework.context.ApplicationContext}
     */
    public static ApplicationContext getContext() {
        return context;
    }

	/**
     * Return the Vaadin4Spring {@link org.vaadin.spring.events.EventBus}
     * @return the Vaadin4Spring {@link org.vaadin.spring.events.EventBus}
     */
    public static EventBus getEventBus() {
        return context.getBean(EventBus.class);
    }
	
	/**
     * @see {@link import org.springframework.context.ApplicationContextAware}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

	/**
     * @see {@link org.springframework.beans.factory.InitializingBean}
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        logger.debug("{} initialized", getClass().getName());

	}
}
