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
package org.vaadin.spring.security.shared.internal;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.config.VaadinSecurityConfigurer;
import org.vaadin.spring.security.internal.AbstractVaadinSecurity;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinLogoutHandler;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;

/**
 * Implementation of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is participating
 * in the existing Spring Web Security setup.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public class VaadinSharedSecurityImpl extends AbstractVaadinSecurity implements VaadinSecurityConfigurer.Callback {

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

    private VaadinSecurityFilter vaadinSecurityFilter;

    public VaadinSharedSecurityImpl(VaadinSecurityConfigurer configurer) {
        configurer.addCallback(this);
    }

    @Override
    public Authentication login(Authentication authentication, boolean rememberMe) throws Exception {
        if (vaadinSecurityFilter == null) {
            throw new AuthenticationServiceException("VaadinSharedSecurity has not been configured properly");
        }

        final HttpServletRequest request = httpRequestResponseHolder.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("No HttpServletRequest bound to current thread");
        }

        final HttpServletResponse response = httpRequestResponseHolder.getCurrentResponse();
        if (response == null) {
            throw new IllegalStateException("No HttpServletResponse bound to current thread");
        }

        vaadinSecurityFilter.setAuthenticationRequest(authentication);
        vaadinSecurityFilter.doFilter(request, response, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response)
                throws IOException, ServletException {
                logger.warn("VaadinSecurityFilter invoked the filter chain even though this should never happen");
            }
        });

        AuthenticationException authenticationException = vaadinSecurityFilter.getAndResetAuthenticationException();
        if (authenticationException != null) {
            throw authenticationException;
        }

        Authentication successfulAuthentication = vaadinSecurityFilter.getAndResetSuccessfulAuthentication();
        if (successfulAuthentication != null) {
            if (vaadinAuthenticationSuccessHandler != null) {
                vaadinAuthenticationSuccessHandler.onAuthenticationSuccess(successfulAuthentication);
            }
            return successfulAuthentication;
        }

        return null;
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

    /**
     * Makes it possible to replace the
     * {@link org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler} after
     * the bean has been configured.
     */
    public void setVaadinAuthenticationSuccessHandler(
        VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler) {
        this.vaadinAuthenticationSuccessHandler = vaadinAuthenticationSuccessHandler;
    }

    public VaadinAuthenticationSuccessHandler getVaadinAuthenticationSuccessHandler() {
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

    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        return sessionAuthenticationStrategy;
    }

    /**
     * Makes it possible to replace the {@link org.vaadin.spring.security.web.authentication.VaadinLogoutHandler} after
     * the bean has been configured.
     */
    public void setVaadinLogoutHandler(VaadinLogoutHandler vaadinLogoutHandler) {
        this.vaadinLogoutHandler = vaadinLogoutHandler;
    }

    public VaadinLogoutHandler getVaadinLogoutHandler() {
        return vaadinLogoutHandler;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        vaadinSecurityFilter = new VaadinSecurityFilter();
        vaadinSecurityFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));

        SessionAuthenticationStrategy sessionAuthenticationStrategy = httpSecurity
            .getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            vaadinSecurityFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }

        RememberMeServices rememberMeServices = httpSecurity.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            vaadinSecurityFilter.setRememberMeServices(rememberMeServices);
        }
    }

    protected static class VaadinSecurityFilter extends AbstractAuthenticationProcessingFilter {

        private final ThreadLocal<Authentication> authenticationRequest = new ThreadLocal<Authentication>();
        private final ThreadLocal<AuthenticationException> authenticationException = new ThreadLocal<AuthenticationException>();
        private final ThreadLocal<Authentication> successfulAuthentication = new ThreadLocal<Authentication>();

        protected VaadinSecurityFilter() {
            super(new RequestMatcher() {
                @Override
                public boolean matches(HttpServletRequest request) {
                    return true;
                }
            });
            setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                    authenticationException.set(exception);
                }
            });
            setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                    successfulAuthentication.set(authentication);
                }
            });
        }

        protected void setAuthenticationRequest(Authentication authenticationRequest) {
            if (authenticationRequest == null) {
                this.authenticationRequest.remove();
            } else {
                this.authenticationRequest.set(authenticationRequest);
            }
        }

        protected AuthenticationException getAndResetAuthenticationException() {
            try {
                return authenticationException.get();
            } finally {
                authenticationException.remove();
            }
        }

        protected Authentication getAndResetSuccessfulAuthentication() {
            try {
                return successfulAuthentication.get();
            } finally {
                successfulAuthentication.remove();
            }
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
            successfulAuthentication.remove();
            authenticationException.remove();
            super.doFilter(req, res, chain);
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
            Authentication authentication = authenticationRequest.get();
            try {
                if (authentication == null) {
                    throw new AuthenticationServiceException("No authentication request bound to current thread");
                }
                return getAuthenticationManager().authenticate(authentication);
            } finally {
                authenticationRequest.remove();
            }
        }
    }
}
