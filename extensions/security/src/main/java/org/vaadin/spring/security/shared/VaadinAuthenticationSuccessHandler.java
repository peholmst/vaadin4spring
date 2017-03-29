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
package org.vaadin.spring.security.shared;

import org.springframework.security.core.Authentication;

/**
 * Strategy used to handle a successful user authentication.
 * <p/>
 * Implementations can do whatever they want but typical behaviour would be to control the navigation to the
 * subsequent destination (using a redirect or a forward). For example, after a user has logged in by submitting a
 * login form, the application needs to decide where they should be redirected to afterwards.
 * Other logic may also be included if required.
 * <p/>
 * Vaadin Specific Strategy of {@link org.springframework.security.web.authentication.AuthenticationSuccessHandler}
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface VaadinAuthenticationSuccessHandler {

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param authentication the <tt>Authentication</tt> object which was created during the authentication process.
     */
    void onAuthenticationSuccess(Authentication authentication) throws Exception;

    /**
     * Implementation of {@link VaadinAuthenticationSuccessHandler} that does nothing.
     */
    final class NullHandler implements VaadinAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(Authentication authentication) throws Exception {
            throw new UnsupportedOperationException();
        }
    }
}
