/*
 * Copyright 2015, 2016 The original authors
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

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.RememberMeServices;

/**
 * Interface which provides access to basic Security Context objects.
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinSecurityContext {

    /**
     * Returns the current Spring application context (never {@code null}).
     */
    ApplicationContext getApplicationContext();

    /**
     * Returns the {@link org.springframework.security.authentication.AuthenticationManager} or {@code null} if not
     * available.
     */
    AuthenticationManager getAuthenticationManager();

    /**
     * Checks if an {@link AuthenticationManager} is available or not. When this method returns true,
     * {@link #getAuthenticationManager()} always returns a non-null object.
     */
    boolean hasAuthenticationManager();

    /**
     * Returns the {@link org.springframework.security.access.AccessDecisionManager} or {@code null} if not available.
     */
    AccessDecisionManager getAccessDecisionManager();

    /**
     * Checks if an {@link AccessDecisionManager} is available or not. When this method returns true,
     * {@link #getAccessDecisionManager()} will always returns a non-null object.
     */
    boolean hasAccessDecisionManager();

    /**
     * Returns the {@link org.springframework.security.web.authentication.RememberMeServices} or an instance of
     * {@link org.springframework.security.web.authentication.NullRememberMeServices} if not available.
     */
    RememberMeServices getRememberMeServices();
}
