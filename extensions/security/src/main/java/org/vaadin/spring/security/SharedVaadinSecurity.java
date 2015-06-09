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

/**
 * Extended version of {@link org.vaadin.spring.security.VaadinSecurity} that is used when shared security
 * is enabled, i.e. Vaadin participates in the existing Spring Web Security setup.
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface SharedVaadinSecurity extends VaadinSecurity, SharedVaadinSecurityContext {

    /**
     * Allows the session attribute name to be customized for this repository instance.
     *
     * @param springSecurityContextKey the key under which the security context will be stored. Defaults to
     */
    void setSpringSecurityContextKey(String springSecurityContextKey);

    /**
     * Set the logout processing URL, defaults to '/logout'.
     * This property should match the configured value with HttpSecurity configuration.
     *
     * @param logoutUrl the use url at which the logout is configured with HttpSecurity
     */
    void setLogoutProcessingUrl(String logoutUrl);
}
