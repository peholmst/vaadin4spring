package org.vaadin.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.Assert;

/**
 * Abstract implementation for the {@link org.vaadin.spring.security.VaadinSecurity}
 * 
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public abstract class AbstractVaadinSecurity implements ApplicationContextAware, InitializingBean, VaadinSecurityContext {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ApplicationContext applicationContext;
	private AuthenticationManager authenticationManager;
	private AccessDecisionManager accessDecisionManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(applicationContext, "Failed to Autowire <ApplicationContext>");
		
		AuthenticationManager authenticationManager;
    	try {
            authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        } catch (NoSuchBeanDefinitionException ex) {
            authenticationManager = null;
            logger.warn("No AuthenticationManager set! Some security methods will not be available.");
        }

        AccessDecisionManager accessDecisionManager;
        try {
            accessDecisionManager = applicationContext.getBean(AccessDecisionManager.class);
        } catch (NoSuchBeanDefinitionException ex) {
            accessDecisionManager = null;
            logger.warn("No AccessDecisionManager set! Some security methods will not be available.");
        }
        
        this.authenticationManager = authenticationManager;
        this.accessDecisionManager = accessDecisionManager;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuthenticationManager getAuthenticationManager() {
        if (authenticationManager == null) {
            throw new IllegalStateException("No AuthenticationManager has been set");
        }
        return authenticationManager;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccessDecisionManager getAccessDecisionManager() {
		return accessDecisionManager;
	}
	
}
