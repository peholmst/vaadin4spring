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

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Strategy used to handle a failed authentication attempt.
 * <p>
 * Typical behaviour might be to redirect the user to the authentication page (in the case of a form login) to
 * allow them to try again. More sophisticated logic might be implemented depending on the type of the exception.
 * For example, a {@link org.springframework.security.authentication.CredentialsExpiredException} might cause a redirect to a web controller which allowed the
 * user to change their password.
 * <p>
 * Vaadin Specific Strategy of {@link AuthenticationFailureHandler}
 */
public interface VaadinAuthenticationFailureHandler {

    /**
     * Called when an authentication attempt fails.
     * @param exception the exception which was thrown to reject the authentication request.
     */
    void onAuthenticationFailure(AuthenticationException exception) throws Exception;
}
