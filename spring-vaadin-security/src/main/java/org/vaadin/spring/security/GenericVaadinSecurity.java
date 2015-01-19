/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Convenience class that provides the Spring Security operations that are most commonly required in a Vaadin application.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public class GenericVaadinSecurity extends AbstractVaadinSecurity implements VaadinSecurity {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionAuthenticationStrategy sessionStrategy;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated() {
        final Authentication authentication = getAuthentication();
        return ( authentication != null && authentication.isAuthenticated() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void login(Authentication authentication) throws AuthenticationException {
        final Authentication fullyAuthenticated = getAuthenticationManager().authenticate(authentication);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(fullyAuthenticated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void login(String username, String password) throws AuthenticationException {
        login(new UsernamePasswordAuthenticationToken(username, password));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAuthority(String authority) {
        final Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication getAuthentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        return authentication;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAccessToObject(Object securedObject, String... securityConfigurationAttributes) {
        final Authentication authentication = getAuthentication();
        if (getAccessDecisionManager() == null || authentication == null || !authentication.isAuthenticated()) {
            if (getAccessDecisionManager() == null) {
                logger.warn("Access was denied to object because there was no AccessDecisionManager set!");
            }
            return false;
        }

        final Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>(securityConfigurationAttributes.length);
        for (String securityConfigString : securityConfigurationAttributes) {
            configAttributes.add(new SecurityConfig(securityConfigString));
        }

        try {
            getAccessDecisionManager().decide(authentication, securedObject, configAttributes);
            return true;
        } catch (AccessDeniedException ex) {
            return false;
        } catch (InsufficientAuthenticationException ex) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAccessToSecuredObject(Object securedObject) {
        final Secured secured = AopUtils.getTargetClass(securedObject).getAnnotation(Secured.class);
        Assert.notNull(secured, "securedObject did not have @Secured annotation");
        return hasAccessToObject(securedObject, secured.value());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAccessToSecuredMethod(Object securedObject, String methodName, Class<?>... methodParameterTypes) {

        try {
            final Method method = securedObject.getClass().getMethod(methodName, methodParameterTypes);
            final Secured secured = AnnotationUtils.findAnnotation(method, Secured.class);
            Assert.notNull(secured, "securedObject did not have @Secured annotation");
            return hasAccessToObject(securedObject, secured.value());
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("Method " + methodName + " does not exist", ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAuthorities(String... authorities) {

        for (String authority : authorities) {
            if (!hasAuthority(authority)) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAnyAuthority(String... authorities) {

        for (String authority : authorities) {
            if (hasAuthority(authority)) {
                return true;
            }
        }

        return false;
    }
}
