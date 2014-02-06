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

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

/**
 * Convenience interface that provides the Spring Security operations that are most commonly required in a Vaadin application.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface Security extends Serializable {

    /**
     * Checks if the current user is authenticated or not.
     */
    boolean isAuthenticated();

    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method
     * will return without exceptions.
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @throws AuthenticationException if authentication failed.
     */
    void login(Authentication authentication) throws AuthenticationException;

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @throws AuthenticationException if authentication failed.
     */
    void login(String username, String password) throws AuthenticationException;

    /**
     * Logs the user out, clearing the {@link org.springframework.security.core.context.SecurityContext} without
     * invalidating the session.
     */
    void logout();

    /**
     * Checks if the current user is authorized based on the specified security configuration attributes. The attributes
     * can be roles or Spring EL expressions (basically anything you can specify as values of the {@link org.springframework.security.access.annotation.Secured} annotation).
     *
     * @param securityConfigurationAttributes the security configuration attributes.
     * @return true if the current user is authorized, false if not.
     */
    boolean isAuthorized(String... securityConfigurationAttributes);

    /**
     * Convenience method that invokes {@link #isAuthorized(String...)}, passing in the value of the specified {@link org.springframework.security.access.annotation.Secured} annotation instance.
     *
     * @param securityConfiguration the security annotation instance.
     * @return true if the current user is authorized, false if not.
     */
    boolean isAuthorized(Secured securityConfiguration);

}
