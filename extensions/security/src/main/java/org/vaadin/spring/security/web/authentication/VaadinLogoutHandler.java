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
package org.vaadin.spring.security.web.authentication;

/**
 * When using shared security and invoking {@link org.vaadin.spring.security.VaadinSecurity#logout()}, a logout handler will
 * be retrieved from the application context and invoked.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.security.web.authentication.VaadinRedirectLogoutHandler
 */
public interface VaadinLogoutHandler {

    /**
     * Called when the user should be logged out.
     */
    void onLogout();

    /**
     * Implementation of {@link org.vaadin.spring.security.web.authentication.VaadinLogoutHandler} that does nothing.
     */
    static final class NullHandler implements VaadinLogoutHandler {

        @Override
        public void onLogout() {
        }
    }
}
