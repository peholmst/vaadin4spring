package org.vaadin.spring.security;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Interface which provides access to basic Security Context objects.
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public interface VaadinSecurityContext {

	/**
	 * Get the current Spring ApplicationContext
	 * 
	 * @return {@link org.springframework.context.ApplicationContext}
	 */
	ApplicationContext getApplicationContext();
	
	/**
	 * Get the configured {@link org.springframework.security.authentication.AuthenticationManager}
	 * return {@code null} if not available.
	 * 
	 * @return {@link org.springframework.security.authentication.AuthenticationManager}
	 */
	AuthenticationManager getAuthenticationManager();
	
	/**
	 * Get the configured {@link org.springframework.security.access.AccessDecisionManager}
	 * return {@code null} if not available.
	 * 
	 * @return {@link org.springframework.security.access.AccessDecisionManager}
	 */
	AccessDecisionManager getAccessDecisionManager();
	
	/** 
     * Checks if the Security bean has an accessDecisionManager
     * 
     * @return true if the Security bean has an accessDecisionManager
     */
	boolean hasAccessDecisionManager();
}
