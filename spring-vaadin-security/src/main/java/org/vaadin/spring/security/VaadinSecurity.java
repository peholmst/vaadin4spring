package org.vaadin.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Interface that provides the Spring Security operations that are most commonly required
 * in a Vaadin application.
 *  
 *  @author Petter Holmström (petter@vaadin.com)
 *  @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public interface VaadinSecurity extends VaadinSecurityContext {

	/**
     * Checks if the current user is authenticated.
     *
     * @return true if the current {@link org.springframework.security.core.context.SecurityContext} contains an {@link org.springframework.security.core.Authentication} token,
     * and the token has been authenticated by an {@link org.springframework.security.authentication.AuthenticationManager}.
     * @see org.springframework.security.core.Authentication#isAuthenticated()
     */
    boolean isAuthenticated();
	
    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method
     * will return without exceptions.
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    void login(Authentication authentication) throws AuthenticationException;
    
    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @throws AuthenticationException if authentication fails.
     */
    void login(String username, String password) throws AuthenticationException;
    
    /**
     * Logs the user out, clearing the {@link org.springframework.security.core.context.SecurityContext} without
     * invalidating the session.
     */
    void logout();
    
    /**
     * Checks if the current user has the specified authority. This method works with static authorities (such as roles).
     * If you need more dynamic authorization (such as ACLs or EL expressions), use {@link #hasAccessToObject(Object, String...)}.
     *
     * @param authority the authority to check, must not be {@code null}.
     * @return true if the current {@link org.springframework.security.core.context.SecurityContext} contains an authenticated {@link org.springframework.security.core.Authentication}
     * token that has a {@link org.springframework.security.core.GrantedAuthority} whose string representation matches the specified {@code authority}.
     * @see org.springframework.security.core.Authentication#getAuthorities()
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
    public boolean hasAuthority(String authority);
    
    /**
     * Gets the authentication token of the current user.
     *
     * @return the {@link org.springframework.security.core.Authentication} token stored in the current {@link org.springframework.security.core.context.SecurityContext}, or {@code null}.
     */
    Authentication getAuthentication();
    
    /**
     * Checks if the current user is authorized based on the specified security configuration attributes. The attributes
     * can be roles or Spring EL expressions (basically anything you can specify as values of the {@link org.springframework.security.access.annotation.Secured} annotation).
     *
     * @param securedObject                   the secured object.
     * @param securityConfigurationAttributes the security configuration attributes.
     * @return true if the current user is authorized, false if not.
     */
    boolean hasAccessToObject(Object securedObject, String... securityConfigurationAttributes);
    
    /**
     * Convenience method that invokes {@link #hasAccessToObject(Object, String...)}, using the {@link org.springframework.security.access.annotation.Secured} annotation of the secured object
     * to get the security configuration attributes.
     *
     * @param securedObject the secured object, must not be {@code null} and must have the {@link org.springframework.security.access.annotation.Secured} annotation.
     * @return true if the current user is authorized, false if not.
     */
    boolean hasAccessToSecuredObject(Object securedObject);
    
    /**
     * Uses the {@link org.springframework.security.access.annotation.Secured} annotation on the specified method to check if the current user has access to the secured object.
     *
     * @param securedObject        the secured object, must not be {@code null}.
     * @param methodName           the name of the method holding the {@link org.springframework.security.access.annotation.Secured} annotation.
     * @param methodParameterTypes the parameter types of the method holding the {@link org.springframework.security.access.annotation.Secured} annotation.
     * @return true if the current user is authorized, false if not.
     * @see #hasAccessToSecuredObject(Object)
     */
    boolean hasAccessToSecuredMethod(Object securedObject, String methodName, Class<?>... methodParameterTypes);
    
    /**
     * Checks if the current user has all required authorities.
     *
     * @param authorities the required authorities.
     * @return true if the current user is authenticated and has all of the specified authorities.
     * @see #hasAuthority(String)
     * @see #hasAnyAuthority(String...)
     */
    boolean hasAuthorities(String... authorities);
    
    /**
     * Checks if the current user has at least one of the specified authorities.
     *
     * @param authorities the authorities.
     * @return true if the current user is authenticated and has at least one of the specified authorities.
     * @see #hasAuthority(String)
     * @see #hasAuthorities(String...)
     */
    boolean hasAnyAuthority(String... authorities);
    
}
