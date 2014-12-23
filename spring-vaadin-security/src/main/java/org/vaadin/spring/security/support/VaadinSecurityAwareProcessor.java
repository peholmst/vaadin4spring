package org.vaadin.spring.security.support;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;
import org.vaadin.spring.security.VaadinSecurityContext;
import org.vaadin.spring.security.VaadinSecurityContextAware;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the VaadinSecurity to beans that
 * implement the {@link org.vaadin.spring.security.VaadinSecurityAware} interface
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @since 12.23.2014
 * @see org.vaadin.spring.security.VaadinSecurityAware
 * @see org.springframework.context.support.ApplicationContextAwareProcessor
 */
public class VaadinSecurityAwareProcessor implements ApplicationContextAware, BeanPostProcessor {

	private ConfigurableApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
	
	@Override
	public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

		AccessControlContext acc = null;

		if ( System.getSecurityManager() != null && (bean instanceof VaadinSecurityAware) ) {
			acc = this.applicationContext.getBeanFactory().getAccessControlContext();
		}
		
		if (acc != null) {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				
				@Override
				public Object run() {
					invokeAwareInterfaces(bean);
					return null;
				}
				
			}, acc);
		}
		else {
			invokeAwareInterfaces(bean);
		}

		return bean;
		
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	private void invokeAwareInterfaces(Object bean) {
		
		if ( bean instanceof Aware ) {
			
			if ( bean instanceof VaadinSecurityAware ) {
				((VaadinSecurityAware) bean).setVaadinSecurity(this.applicationContext.getBean(VaadinSecurity.class));
			}
		
			if ( bean instanceof VaadinSecurityContextAware ) {
				((VaadinSecurityContextAware) bean).setVaadinSecurityContext(this.applicationContext.getBean(VaadinSecurityContext.class));
			}
		}
		
	}

}
