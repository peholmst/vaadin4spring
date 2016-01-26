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
package org.vaadin.spring.security.shared;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.Assert;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.AbstractVaadinSecurity;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;

/**
 * Default implementation of {@link VaadinSharedSecurity}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public class DefaultVaadinSharedSecurity extends AbstractVaadinSecurity implements VaadinSharedSecurity {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVaadinSharedSecurity.class);

    @Autowired
    HttpService httpService;
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
        final HttpServletRequest request = new RememberMeRequestWrapper(getCurrentRequest(), rememberMe);
        final HttpServletResponse response = getCurrentResponse();

        try {
            LOGGER.debug("Attempting authentication of {}, rememberMe = {}", authentication, rememberMe);
            final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);

            LOGGER.debug("Invoking session authentication strategy");
            sessionAuthenticationStrategy.onAuthentication(fullyAuthenticated, request, response);

            successfulAuthentication(fullyAuthenticated, request, response);
            return fullyAuthenticated;
        } catch (Exception e) {
            unsuccessfulAuthentication(request, response);
            throw e;
        } finally {
            if (saveContextInSessionAfterLogin) {
                LOGGER.debug("Saving security context in the session");
                WrappedSession session = getSession();
                if (session != null) {
                    session.setAttribute(springSecurityContextKey, SecurityContextHolder.getContext());
                } else {
                    LOGGER.warn(
                        "Tried to save security context in the session, but no session was bound to the current thread");
                }
            }
        }
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("Authentication failed");
        SecurityContextHolder.clearContext();
        getRememberMeServices().loginFail(request, response);
    }

    protected void successfulAuthentication(Authentication authentication, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        LOGGER.debug("Authentication succeeded");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        getRememberMeServices().loginSuccess(request, response, authentication);
        vaadinAuthenticationSuccessHandler.onAuthenticationSuccess(authentication);
    }

    protected HttpServletRequest getCurrentRequest() {
        final HttpServletRequest request = httpService.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("No HttpServletRequest bound to current thread");
        }
        return request;
    }

    protected HttpServletResponse getCurrentResponse() {
        final HttpServletResponse response = httpService.getCurrentResponse();
        if (response == null) {
            throw new IllegalStateException("No HttpServletResponse bound to current thread");
        }
        return response;
    }

    @Override
    public Authentication login(String username, String password, boolean rememberMe)
        throws AuthenticationException, Exception {
        return login(new UsernamePasswordAuthenticationToken(username, password), rememberMe);
    }

    @Override
    public Authentication login(Authentication authentication) throws AuthenticationException, Exception {
        return login(authentication, false);
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
            LOGGER.debug("No authentication object bound to thread, trying to access the session directly");
            WrappedSession session = getSession();
            if (session != null) {
                SecurityContext context = (SecurityContext) session.getAttribute(springSecurityContextKey);
                authentication = context.getAuthentication();
            } else {
                LOGGER.debug("No session bound to current thread, cannot retrieve the authentication object");
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
     * Sets the session attribute key under which the security context is stored. Defaults to
     * {@link HttpSessionSecurityContextRepository#SPRING_SECURITY_CONTEXT_KEY}.
     */
    public void setSpringSecurityContextKey(String springSecurityContextKey) {
        Assert.hasText(springSecurityContextKey, "springSecurityContextKey cannot be empty");
        this.springSecurityContextKey = springSecurityContextKey;
    }

    /**
     * Specifies whether the security context should be explicitly saved in the session after
     * {@link #login(org.springframework.security.core.Authentication, boolean)}
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
            LOGGER.info("No session authentication strategy found in application context, using null strategy");
            sessionAuthenticationStrategy = new NullAuthenticatedSessionStrategy();
        } else {
            LOGGER.info("Using session authentication strategy {}", sessionAuthenticationStrategy);
        }
        if (vaadinAuthenticationSuccessHandler == null) {
            LOGGER.info("No authentication success handler found in the application context, using null handler");
            vaadinAuthenticationSuccessHandler = new VaadinAuthenticationSuccessHandler.NullHandler();
        } else {
            LOGGER.info("Using authentication success handler {}", vaadinAuthenticationSuccessHandler);
        }
        if (vaadinLogoutHandler == null) {
            LOGGER.info("No logout handler found in the application context, using null handler");
            vaadinLogoutHandler = new VaadinLogoutHandler.NullHandler();
        } else {
            LOGGER.info("Using logout handler {}", vaadinLogoutHandler);
        }
    }

    /**
     * Makes it possible to replace the
     * {@link VaadinAuthenticationSuccessHandler} after
     * the bean has been configured.
     */
    public void setVaadinAuthenticationSuccessHandler(
        VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler) {
        this.vaadinAuthenticationSuccessHandler = vaadinAuthenticationSuccessHandler;
    }

    protected VaadinAuthenticationSuccessHandler getVaadinAuthenticationSuccessHandler() {
        return vaadinAuthenticationSuccessHandler;
    }

    /**
     * Makes it possible to replace the
     * {@link org.springframework.security.web.authentication.session.SessionAuthenticationStrategy} after
     * the bean has been configured.
     */
    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    protected SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        return sessionAuthenticationStrategy;
    }

    /**
     * Makes it possible to replace the {@link VaadinLogoutHandler} after
     * the bean has been configured.
     */
    public void setVaadinLogoutHandler(VaadinLogoutHandler vaadinLogoutHandler) {
        this.vaadinLogoutHandler = vaadinLogoutHandler;
    }

    protected VaadinLogoutHandler getVaadinLogoutHandler() {
        return vaadinLogoutHandler;
    }

    protected class RememberMeRequestWrapper extends HttpServletRequestWrapper {

        public RememberMeRequestWrapper(HttpServletRequest request, boolean rememberMe) {
            super(request);
        }
    }
}
