package org.vaadin.spring.security;

import org.springframework.beans.factory.Aware;

/**
 * Interface to be implemented by any object that wishes to be notified 
 * of the {@link org.vaadin.spring.security.VaadinSecurity}.
 *  
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public interface VaadinSecurityAware extends Aware {

	/**
	 * Set the VaadinSecurity.
	 * <p>Invoked after population of normal bean properties but before an init callback such
	 * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
	 * or a custom init-method.
	 * 
	 * @param vaadinSecurity the VaadinSecurity object used within the applicationContext.
	 */
	void setVaadinSecurity(VaadinSecurity vaadinSecurity);
}
