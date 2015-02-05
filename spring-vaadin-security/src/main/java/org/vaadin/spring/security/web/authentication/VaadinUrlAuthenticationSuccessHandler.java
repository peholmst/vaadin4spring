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

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;

/**
 * <tt>VaadinAuthenticationSuccessHandler</tt> which can be configured with a default URL which users should be
 * sent to upon successful authentication.
 * <p>
 * The logic used is that of the {@link AbstractVaadinAuthenticationTargetUrlRequestHandler parent class}.
 * <p>
 * Must be used as a bean because of autowiring within parent class.
 * 
 * @author Luke Taylor (original source code of {@link org.springframework.security.web.authentication.AuthenticationSuccessHandler})
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com) (Vaadin specific implementation)
 */
public class VaadinUrlAuthenticationSuccessHandler extends AbstractVaadinAuthenticationTargetUrlRequestHandler implements VaadinAuthenticationSuccessHandler {

    public VaadinUrlAuthenticationSuccessHandler() {}
    
    public VaadinUrlAuthenticationSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }
    
    @Override
    public void onAuthenticationSuccess(Authentication authentication) throws Exception {
        handle(authentication);
        clearAuthenticationAttributes();
    }

    /**
     * Removes temporary authentication-related data which may have been stored in the session
     * during the authentication process.
     */
    protected final void clearAuthenticationAttributes() {
        HttpSession session = http.getCurrentRequest().getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
