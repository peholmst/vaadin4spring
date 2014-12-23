package org.vaadin.spring.security;

/**
 * Interface to be implemented by any object that wishes to be notified
 * of the {@link org.vaadin.spring.security.VaadinSecurityContext}.
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public interface VaadinSecurityContextAware {

	/**
	 * Set the VaadinSecurityContext.
	 * <p>Invoked after population of normal bean properties but before an init callback such
	 * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
	 * or a custom init-method.
	 * 
	 * @param vaadinSecurityContext the VaadinSecurityContext object.
	 */
	void setVaadinSecurityContext(VaadinSecurityContext vaadinSecurityContext);
	
}
