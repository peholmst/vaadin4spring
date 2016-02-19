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
package org.vaadin.spring.security.managed;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.security.AbstractVaadinSecurity;

import com.vaadin.server.*;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;

/**
 * Default implementation of {@link org.vaadin.spring.security.managed.VaadinManagedSecurity}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class DefaultVaadinManagedSecurity extends AbstractVaadinSecurity implements VaadinManagedSecurity {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVaadinManagedSecurity.class);

    public static final String REINITIALIZE_SESSION_AFTER_LOGIN_PROPERTY = "vaadin.spring.security.managed.reinitialize-session-after-login";

    public DefaultVaadinManagedSecurity() {
        LOGGER.info("Using Vaadin Managed Security");
    }

    /**
     * Returns whether the current session should be reinitialized after a successful login. Default is true.
     * Can be disabled by setting the "{@value #REINITIALIZE_SESSION_AFTER_LOGIN_PROPERTY}" environment property to
     * false. If Websocket Push is used, the session will never be reinitialized since this throws errors on at least
     * Tomcat 8.
     */
    protected boolean isReinitializeSessionAfterLogin() {
        return getApplicationContext().getEnvironment().getProperty(REINITIALIZE_SESSION_AFTER_LOGIN_PROPERTY,
            Boolean.class, true);
    }

    @Override
    public Authentication login(Authentication authentication) throws AuthenticationException {
        LOGGER.debug("Authenticating using {}", authentication);
        final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);
        if (isReinitializeSessionAfterLogin()) {
            clearAndReinitializeSession();
        }
        SecurityContext context = SecurityContextHolder.getContext();
        LOGGER.debug("Setting authentication of context {} to {}", context, fullyAuthenticated);
        context.setAuthentication(fullyAuthenticated);
        return fullyAuthenticated;
    }

    /**
     * Clears the session of all attributes except some internal Vaadin attributes and reinitializes it. If Websocket
     * Push is used, the session will never be reinitialized since this throws errors on at least
     * Tomcat 8.
     */
    protected void clearAndReinitializeSession() {
        final VaadinRequest currentRequest = VaadinService.getCurrentRequest();
        final UI currentUI = UI.getCurrent();
        if (currentUI != null) {
            final Transport transport = currentUI.getPushConfiguration().getTransport();
            if (Transport.WEBSOCKET.equals(transport) || Transport.WEBSOCKET_XHR.equals(transport)) {
                LOGGER.warn(
                    "Clearing and reinitializing the session is currently not supported when using Websocket Push.");
                return;
            }
        }
        if (currentRequest != null) {
            LOGGER.debug("Clearing the session");
            final WrappedSession session = currentRequest.getWrappedSession();
            final String serviceName = VaadinService.getCurrent().getServiceName();
            final Set<String> attributesToSpare = new HashSet<String>();
            attributesToSpare.add(serviceName + ".lock");
            attributesToSpare.add(VaadinSession.class.getName() + "." + serviceName);
            for (String s : currentRequest.getWrappedSession().getAttributeNames()) {
                if (!attributesToSpare.contains(s)) {
                    LOGGER.trace("Removing attribute {} from session", s);
                    session.removeAttribute(s);
                }
            }
            LOGGER.debug("Reinitializing the session {}", session.getId());
            VaadinService.reinitializeSession(currentRequest);
            LOGGER.debug("Session reinitialized, new ID is {}",
                VaadinService.getCurrentRequest().getWrappedSession().getId());
        } else {
            LOGGER
                .warn("No VaadinRequest bound to current thread, could NOT clear/reinitialize the session after login");
        }
    }

    @Override
    public void logout() {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Override
    public void logout(String logoutUrl) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().setLocation(logoutUrl);
    }

    @Override
    public Authentication getAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication();
    }
}
