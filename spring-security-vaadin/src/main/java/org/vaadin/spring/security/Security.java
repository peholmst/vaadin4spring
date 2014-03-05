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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Convenience class that provides the Spring Security operations that are most commonly required in a Vaadin application.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class Security {

    private final AuthenticationManager authenticationManager;

    private final AccessDecisionManager accessDecisionManager;

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired(required = false)
    public Security(AuthenticationManager authenticationManager, AccessDecisionManager accessDecisionManager) {
        this.authenticationManager = authenticationManager;
        if (authenticationManager == null) {
            logger.warn("No AuthenticationManager set! Some security methods will not be available.");
        }
        this.accessDecisionManager = accessDecisionManager;
        if (accessDecisionManager == null) {
            logger.warn("No AccessDecisionManager set! Some security methods will not be available.");
        }
    }

    private AuthenticationManager getAuthenticationManager() {
        if (authenticationManager == null) {
            throw new IllegalStateException("No AuthenticationManager has been set");
        }
        return authenticationManager;
    }

    /**
     * Checks if the current user is authenticated.
     *
     * @return true if the current {@link org.springframework.security.core.context.SecurityContext} contains an {@link org.springframework.security.core.Authentication} token,
     * and the token has been authenticated by an {@link org.springframework.security.authentication.AuthenticationManager}.
     * @see org.springframework.security.core.Authentication#isAuthenticated()
     */
    public boolean isAuthenticated() {
        final Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method
     * will return without exceptions.
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    public void login(Authentication authentication) throws AuthenticationException {
        final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(fullyAuthenticated);
    }

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @throws AuthenticationException if authentication fails.
     */
    public void login(String username, String password) throws AuthenticationException {
        login(new UsernamePasswordAuthenticationToken(username, password));
    }

    /**
     * Logs the user out, clearing the {@link org.springframework.security.core.context.SecurityContext} without
     * invalidating the session.
     */
    public void logout() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
    }

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
    public boolean hasAuthority(String authority) {
        final Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the authentication token of the current user.
     *
     * @return the {@link org.springframework.security.core.Authentication} token stored in the current {@link org.springframework.security.core.context.SecurityContext}, or {@code null}.
     */
    public Authentication getAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        return authentication;
    }

    /**
     * Checks if the current user is authorized based on the specified security configuration attributes. The attributes
     * can be roles or Spring EL expressions (basically anything you can specify as values of the {@link org.springframework.security.access.annotation.Secured} annotation).
     *
     * @param securedObject                   the secured object.
     * @param securityConfigurationAttributes the security configuration attributes.
     * @return true if the current user is authorized, false if not.
     */
    public boolean hasAccessToObject(Object securedObject, String... securityConfigurationAttributes) {
        final Authentication authentication = getAuthentication();
        if (accessDecisionManager == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        final Collection<ConfigAttribute> configAttributes = new ArrayList<>(securityConfigurationAttributes.length);
        for (String securityConfigString : securityConfigurationAttributes) {
            configAttributes.add(new SecurityConfig(securityConfigString));
        }
        try {
            accessDecisionManager.decide(authentication, securedObject, configAttributes);
            return true;
        } catch (AccessDeniedException | InsufficientAuthenticationException ex) {
            return false;
        }
    }

    /**
     * Convenience method that invokes {@link #hasAccessToObject(Object, String...)}, using the {@link org.springframework.security.access.annotation.Secured} annotation of the secured object
     * to get the security configuration attributes.
     *
     * @param securedObject the secured object, must not be {@code null} and must have the {@link org.springframework.security.access.annotation.Secured} annotation.
     * @return true if the current user is authorized, false if not.
     */
    public boolean hasAccessToSecuredObject(Object securedObject) {
        final Secured secured = securedObject.getClass().getAnnotation(Secured.class);
        Assert.notNull(secured, "securedObject did not have @Secured annotation");
        return hasAccessToObject(securedObject, secured.value());
    }

    /**
     * Checks if the current user has all required authorities.
     *
     * @param authorities the required authorities.
     * @return true if the current user is authenticated and has all of the specified authorities.
     * @see #hasAuthority(String)
     * @see #hasAnyAuthority(String...)
     */
    public boolean hasAuthorities(String... authorities) {
        for (String authority : authorities) {
            if (!hasAuthority(authority)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the current user has at least one of the specified authorities.
     *
     * @param authorities the authorities.
     * @return true if the current user is authenticated and has at least one of the specified authorities.
     * @see #hasAuthority(String)
     * @see #hasAuthorities(String...)
     */
    public boolean hasAnyAuthority(String... authorities) {
        for (String authority : authorities) {
            if (hasAuthority(authority)) {
                return true;
            }
        }
        return false;
    }
}
