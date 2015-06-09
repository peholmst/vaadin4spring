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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.util.Assert;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.SharedVaadinSecurity;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationFailureHandler;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Implementation of {@link org.vaadin.spring.security.VaadinSecurity} that is used when Vaadin is participating
 * in the existing Spring Web Security setup.
 * <p/>
 * Additional Code / Documentation from {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository}
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public class SharedVaadinSecurityImpl extends AbstractVaadinSecurity implements SharedVaadinSecurity {

    /**
     * The default key under which the security context will be stored in the session.
     * Used by {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository}
     * <br><br>
     * If this is overridden by the user then also override {@code springSecurityContextKey} within {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository}
     * to match the new key.
     * <br><br>
     * The key of {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository} can be overridden with {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository#setSpringSecurityContextKey}
     * <br><br>
     * The {@link org.springframework.security.web.context.SecurityContextPersistenceFilter} will use the configured key from {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository}
     */
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private String springSecurityContextKey = SPRING_SECURITY_CONTEXT_KEY;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String logoutProcessingUrl = "/logout";

    /**
     * Current {@link HttpServletRequest} and {@link HttpServletResponse} are
     * autowired through the {@link HttpService} proxy.
     */
    @Autowired
    private HttpService httpRequestResponseHolder;

    @Autowired
    private VaadinRedirectStrategy redirectStrategy;

    @Autowired(required = false)
    private RememberMeServices rememberMeService;

    private VaadinAuthenticationFailureHandler authenticationFailureHandler;
    private VaadinAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    public void login(Authentication authentication, boolean rememberMe) throws AuthenticationException, Exception {

        // Ensure SecurityContext is never null
        SecurityContext context = SecurityContextHolder.getContext();

        // Retrieve HttpServletRequest / HttpServletResponse from RequestScope
        HttpServletRequest request = httpRequestResponseHolder.getCurrentRequest();
        HttpServletResponse response = httpRequestResponseHolder.getCurrentResponse();

        try {
            
            /*
             * Try to authenticate user
             */
            final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);
            
            /*
             * Store Authentication within SecurityContext
             */
            context.setAuthentication(fullyAuthenticated);
            
            /*
             * Handle RememberMe
             */
            if (rememberMe) {
                
                /*
                 * Locate RememberMeService
                 */
                if (rememberMeService != null) {
                    
                    /*
                     * Store RememberMe within HttpServletRequest
                     */
                    logger.debug("Registering RememberMe in request");
                    request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER, rememberMe);
                    
                    /*
                     * Signal the RememberMeService of the login
                     */
                    rememberMeService.loginSuccess(request, response, authentication);

                } else {
                    logger.error("RememberMe Request while no <RememberMeServices> found within <ApplicationContext>");
                }

            }
            
            /*
             * Signal the SessionAuthenticationStrategy of the login
             */
            getSessionAuthenticationStrategy().onAuthentication(fullyAuthenticated, request, response);
            
            /*
             * Process AuthenticationSuccessHandler if configured
             */
            if (hasAuthenticationSuccessHandlerConfigured()) {
                getAuthenticationSuccessHandler().onAuthenticationSuccess(authentication);
            }

        } catch (AuthenticationException e) {

            /**
             * {@link DisabledException}            -> Optional
             * {@link LockedException}              -> Optional
             * {@link BadCredentialsException}      -> Required
             *
             * Clear SecurityContext and handler Authentication Failure
             * If AuthenticationFailureHandler is configured, use it else
             * throw {@link AuthenticationFailureHandler}
             */
            context = generateNewContext();
            
            /*
             * Handle RememberMe authentication Failure
             * No need to check if it is being used; 
             * on failure invalidate all remember-me-tokens.
             */
            if (rememberMeService != null) {
                rememberMeService.loginFail(request, response);
            }
            
            /*
             * Process AuthenticationFailureHandler if configured
             */
            if (hasAuthenticationFailureHandlerConfigured()) {
                getAuthenticationFailureHandler().onAuthenticationFailure(e);
            } else {
                throw e;
            }

        } finally {

            /**
             * Store SecurityContext within the Session for the SecurityFilterChain
             * On error this will store an empty context, which will cause
             * the security filter chain to invalidate the authentication.
             *
             * Context needs to be stored within the HttpSession so the {@link SecurityContextPersistenceFilter}
             * can make a call to the default configured {@link HttpSessionSecurityContextRepository}
             */
            HttpSession session = httpRequestResponseHolder.getCurrentRequest().getSession();
            session.setAttribute(springSecurityContextKey, context);

        }


    }


    @Override
    public void setLogoutProcessingUrl(String logoutUrl) {
        logoutProcessingUrl = logoutUrl;
    }

    @Override
    public void logout() {
        /*
         * Redirect user to the logout URL and have the configured LogoutFilter
         * with {@link LogoutHandlers} handle the logout.
         */
        redirectStrategy.sendRedirect(logoutProcessingUrl);
    }

    @Override
    public Authentication getAuthentication() {
        /*
         * Fetch the Authentication object.
         * Authentication is not available within the SecurityContextHolder.
         * 
         * The SecurityContextHolder only holds the Authentication when its
         * processing the securityFilterChain. After it completes the chain
         * it clears the context holder due to security reasons.
         * 
         * Therefor the Authentication object can be retrieved from the
         * location where the securityFilterChain or VaadinSecurity has left it,
         * within the HttpSession.
         * 
         * Due to work flow or maybe access to the Authentication object through
         * VaadinSecurity form custom changes to the securityFilterChain, first the
         * securityContextHolder is checked.
         */

        final SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            /*
             * Fetch the Current HttpSession from the RequestScope
             * because the chain already was completed and therefor
             * the SecurityContextHolder cleared
             */
            HttpSession httpSession = httpRequestResponseHolder.getCurrentRequest().getSession();
            authentication = ((org.springframework.security.core.context.SecurityContextImpl) httpSession.getAttribute(springSecurityContextKey)).getAuthentication();
        }

        return authentication;
    }

    @Override
    public void setSpringSecurityContextKey(String springSecurityContextKey) {
        Assert.hasText(springSecurityContextKey, "springSecurityContextKey cannot be empty");
        this.springSecurityContextKey = springSecurityContextKey;
    }

    /**
     * By default, calls {@link SecurityContextHolder#createEmptyContext()} to obtain a new context (there should be
     * no context present in the holder when this method is called). Using this approach the context creation
     * strategy is decided by the {@link SecurityContextHolderStrategy} in use. The default implementations
     * will return a new <tt>SecurityContextImpl</tt>.
     *
     * @return a new SecurityContext instance. Never null.
     */
    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        return null;
    }

    protected VaadinAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return authenticationSuccessHandler;
    }

    @Override
    public void setAuthenticationSuccessHandler(VaadinAuthenticationSuccessHandler handler) {
        this.authenticationSuccessHandler = handler;
    }

    @Override
    public boolean hasAuthenticationSuccessHandlerConfigured() {
        return authenticationSuccessHandler != null;
    }

    protected VaadinAuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    @Override
    public void setAuthenticationFailureHandler(VaadinAuthenticationFailureHandler handler) {
        this.authenticationFailureHandler = handler;
    }

    @Override
    public boolean hasAuthenticationFailureHandlerConfigured() {
        return authenticationFailureHandler != null;
    }
}
