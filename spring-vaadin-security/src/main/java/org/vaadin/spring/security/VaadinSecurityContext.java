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
