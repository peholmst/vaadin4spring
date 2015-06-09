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
package org.vaadin.spring.security.internal;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is managing the
 * security of the web application.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ManagedVaadinSecurityImpl extends AbstractVaadinSecurity {

    private static final Logger logger = LoggerFactory.getLogger(ManagedVaadinSecurityImpl.class);

    public ManagedVaadinSecurityImpl() {
        logger.info("Using Vaadin Managed Security");
    }

    @Override
    public void login(Authentication authentication, boolean rememberMe) throws AuthenticationException {
        SecurityContext context = SecurityContextHolder.getContext();
        logger.debug("Authenticating using {}, rememberMe = {}", authentication, rememberMe);
        final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);
        logger.debug("Setting authentication of context {} to {}", context, fullyAuthenticated);
        context.setAuthentication(fullyAuthenticated);
        if (rememberMe) {
            logger.warn("Support for Remember Me has not been added yet");
            // TODO Implement me!
        }
    }

    @Override
    public void logout() {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Override
    public Authentication getAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication();
    }
}
