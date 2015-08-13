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

import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.Assert;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is participating
 * in the existing Spring Web Security setup.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public class VaadinSharedSecurity extends AbstractVaadinSecurity {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    HttpService httpRequestResponseHolder;
    @Autowired(required = false)
    SessionAuthenticationStrategy sessionAuthenticationStrategy;
    @Autowired(required = false)
    VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler;
    @Autowired(required = false)
    VaadinLogoutHandler vaadinLogoutHandler;
    private String springSecurityContextKey = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    private boolean saveContextInSessionAfterLogin = false;

    @Override
    public Authentication login(Authentication authentication, boolean rememberMe) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();

        final HttpServletRequest request = httpRequestResponseHolder.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("No HttpServletRequest bound to current thread");
        }

        final HttpServletResponse response = httpRequestResponseHolder.getCurrentResponse();
        if (response == null) {
            throw new IllegalStateException("No HttpServletResponse bound to current thread");
        }

        try {
            logger.debug("Attempting authentication of {}, rememberMe = {}", authentication, rememberMe);
            final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);
            context.setAuthentication(fullyAuthenticated);
            if (rememberMe) {
                if (hasRememberMeServices()) {
                    logger.debug("Invoking RememberMeServices");
                    getRememberMeServices().loginSuccess(request, response, authentication);
                } else {
                    throw new IllegalStateException("Requested RememberMe authentication but no RememberBeServices are available");
                }
            }
            logger.debug("Invoking session authentication strategy");
            sessionAuthenticationStrategy.onAuthentication(fullyAuthenticated, request, response);
            logger.debug("Invoking authentication success handler");
            vaadinAuthenticationSuccessHandler.onAuthenticationSuccess(fullyAuthenticated);
            return authentication;
        } catch (AuthenticationException e) {
            logger.debug("Authentication failed");
            context = SecurityContextHolder.createEmptyContext();
            if (hasRememberMeServices()) {
                logger.debug("Invoking RememberMeServices");
                getRememberMeServices().loginFail(request, response);
            }
            throw e;
        } finally {
            if (saveContextInSessionAfterLogin) {
                logger.debug("Saving security context in the session");
                WrappedSession session = getSession();
                if (session != null) {
                    session.setAttribute(springSecurityContextKey, context);
                } else {
                    logger.warn("Tried to save security context in the session, but no session was bound to the current thread");
                }
            }
        }
    }

    @Override
    public void logout() {
        vaadinLogoutHandler.onLogout();
    }

    @Override
    public Authentication getAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            // The SecurityContextHolder only holds the Authentication when it is
            // processing the securityFilterChain. After it completes the chain
            // it clears the context holder.

            // Therefore, the Authentication object can be retrieved from the
            // location where the securityFilterChain or VaadinSecurity has left it,
            // within the HttpSession.
            logger.debug("No authentication object bound to thread, trying to access the session directly");
            WrappedSession session = getSession();
            if (session != null) {
                SecurityContext context = (SecurityContext) session.getAttribute(springSecurityContextKey);
                authentication = context.getAuthentication();
            } else {
                logger.debug("No session bound to current thread, cannot retrieve the authentication object");
            }
        }
        return authentication;
    }

    private WrappedSession getSession() {
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            return vaadinSession.getSession();
        } else {
            return null;
        }
    }

    /**
     * Sets the session attribute key under which the security context is stored. Defaults to {@link HttpSessionSecurityContextRepository#SPRING_SECURITY_CONTEXT_KEY}.
     */
    public void setSpringSecurityContextKey(String springSecurityContextKey) {
        Assert.hasText(springSecurityContextKey, "springSecurityContextKey cannot be empty");
        this.springSecurityContextKey = springSecurityContextKey;
    }

    /**
     * Specifies whether the security context should be explicitly saved in the session after {@link #login(org.springframework.security.core.Authentication, boolean)}
     * completes. Defaults to false.
     *
     * @see #setSpringSecurityContextKey(String)
     */
    public void setSaveContextInSessionAfterLogin(boolean saveContextInSessionAfterLogin) {
        this.saveContextInSessionAfterLogin = saveContextInSessionAfterLogin;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (sessionAuthenticationStrategy == null) {
            logger.info("No session authentication strategy found in application context, using null strategy");
            sessionAuthenticationStrategy = new NullAuthenticatedSessionStrategy();
        } else {
            logger.info("Using session authentication strategy {}", sessionAuthenticationStrategy);
        }
        if (vaadinAuthenticationSuccessHandler == null) {
            logger.info("No authentication success handler found in the application context, using null handler");
            vaadinAuthenticationSuccessHandler = new VaadinAuthenticationSuccessHandler.NullHandler();
        } else {
            logger.info("Using authentication success handler {}", vaadinAuthenticationSuccessHandler);
        }
        if (vaadinLogoutHandler == null) {
            logger.info("No logout handler found in the application context, using null handler");
            vaadinLogoutHandler = new VaadinLogoutHandler.NullHandler();
        } else {
            logger.info("Using logout handler {}", vaadinLogoutHandler);
        }
    }
}
