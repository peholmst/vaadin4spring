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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * Convenience class that provides the Spring Security operations that are most commonly required in a Vaadin application.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class Security {

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Checks if the current user is authenticated or not.
     */
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method
     * will return without exceptions.
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @throws org.springframework.security.core.AuthenticationException if authentication failed.
     */
    public void login(Authentication authentication) throws AuthenticationException {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me!
    }

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @throws AuthenticationException if authentication failed.
     */
    public void login(String username, String password) throws AuthenticationException {
        login(new UsernamePasswordAuthenticationToken(username, password));
    }

    /**
     * Logs the user out, clearing the {@link org.springframework.security.core.context.SecurityContext} without
     * invalidating the session.
     */
    public void logout() {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me!
    }

    /**
     * @param authority
     * @return
     */
    public boolean hasAuthority(String authority) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me!
    }

    /**
     * @return
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
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me!
    }

    /**
     * Convenience method that invokes {@link #hasAccessToObject(Object, String...)}, using the {@link org.springframework.security.access.annotation.Secured} annotation of the secured object
     * to get the security configuration attributes.
     *
     * @param securedObject the secured object, must not be {@code null} and must have the {@link org.springframework.security.access.annotation.Secured} annotation.
     * @return true if the current user is authorized, false if not.
     */
    public boolean hasAccessToSecuredObject(Object securedObject) {
        Secured secured = securedObject.getClass().getAnnotation(Secured.class);
        Assert.notNull(secured, "securedObject did not have @Secured annotation");
        return hasAccessToObject(securedObject, secured.value());
    }

    /**
     * @param authorities
     * @return
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
     * @param authorities
     * @return
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
