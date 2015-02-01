/*
 * Copyright 2014 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;

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
     * @throws Exception 
     */
    void login(Authentication authentication) throws AuthenticationException, Exception;

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @throws AuthenticationException if authentication fails.
     * @throws ServletException if Authentication{Success/Failure}Handler fails
     * @throws IOException if Authentication{Success/Failure}Handler fails
     */
    void login(String username, String password) throws AuthenticationException, Exception;

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
    boolean hasAuthority(String authority);

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

    /**
     * Allows the session attribute name to be customized for this repository instance.
     *
     * @param springSecurityContextKey the key under which the security context will be stored. Defaults to
     * {@link #SPRING_SECURITY_CONTEXT_KEY}.
     */
    void setSpringSecurityContextKey(String springSecurityContextKey);
}
