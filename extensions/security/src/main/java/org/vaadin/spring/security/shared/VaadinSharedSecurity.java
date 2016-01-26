/*
 * Copyright 2016 The original authors
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
package org.vaadin.spring.security.shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * Extension of of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is participating
 * in an existing Spring Web Security setup.
 * 
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinSharedSecurity extends VaadinSecurity {

    /**
     * Tries to login using the specified authentication object. If authentication succeeds, this method
     * will return without exceptions.
     *
     * @param authentication the authentication object to authenticate, must not be {@code null}.
     * @param rememberMe boolean to indicate if remember me authentication should be activated
     * @return the authenticated {@code Authentication} token.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    Authentication login(Authentication authentication, boolean rememberMe) throws AuthenticationException, Exception;

    /**
     * Convenience method that invokes {@link #login(org.springframework.security.core.Authentication)} with a
     * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}-object.
     *
     * @param username the username to use, must not be {@code null}.
     * @param password the password to use, must not be {@code null}.
     * @param rememberMe boolean to set remember me authentication
     * @return the authenticated {@code Authentication} token.
     * @throws AuthenticationException if authentication fails.
     */
    Authentication login(String username, String password, boolean rememberMe)
        throws AuthenticationException, Exception;
}
