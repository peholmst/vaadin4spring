/*
 * Copyright 2015 The original authors
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

import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationFailureHandler;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;

/**
 * Extended version of {@link org.vaadin.spring.security.VaadinSecurityContext} that is used when shared security
 * is enabled, i.e. Vaadin participates in the existing Spring Web Security setup.
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface SharedVaadinSecurityContext extends VaadinSecurityContext {

    /**
     * Get the configured {@link org.springframework.security.web.authentication.session.SessionAuthenticationStrategy}
     * return {@link org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy} if not configured
     *
     * @return {@link org.springframework.security.web.authentication.session.SessionAuthenticationStrategy}
     */
    SessionAuthenticationStrategy getSessionAuthenticationStrategy();

    /**
     * Add {@link org.springframework.security.web.authentication.AuthenticationSuccessHandler}
     * <br><br>
     * Set to <code>null</code> to deactivate
     */
    void setAuthenticationSuccessHandler(VaadinAuthenticationSuccessHandler handler);

    /**
     * Check if {@link org.springframework.security.web.authentication.AuthenticationSuccessHandler} is configured
     * <br><br>
     *
     * @return <code>true</code> if configured, else <code>false</code>
     */
    boolean hasAuthenticationSuccessHandlerConfigured();

    /**
     * Add {@link org.springframework.security.web.authentication.AuthenticationFailureHandler}
     * <br><br>
     * Set to <code>null</code> to deactivate
     */
    void setAuthenticationFailureHandler(VaadinAuthenticationFailureHandler handler);

    /**
     * Check if {@link org.springframework.security.web.authentication.AuthenticationFailureHandler} is configured
     * <br><br>
     *
     * @return <code>true</code> if configured, else <code>false</code>
     */
    boolean hasAuthenticationFailureHandlerConfigured();
}
