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
package org.vaadin.spring.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.Assert;

/**
 * Abstract base class for implementations of {@link org.vaadin.spring.security.VaadinSecurity}
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public abstract class AbstractVaadinSecurity implements ApplicationContextAware, InitializingBean, VaadinSecurity {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVaadinSecurity.class);

    private ApplicationContext applicationContext;
    private AuthenticationManager authenticationManager;
    private AccessDecisionManager accessDecisionManager;
    private RememberMeServices rememberMeServices;

    @Override
    public boolean isAuthenticated() {
        final Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean isAuthenticatedAnonymously() {
        final Authentication authentication = getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken && authentication.isAuthenticated();
    }

    @Override
    public boolean isRememberMeAuthenticated() {
        final Authentication authentication = getAuthentication();
        return authentication instanceof RememberMeAuthenticationToken && authentication.isAuthenticated();
    }

    @Override
    public boolean isFullyAuthenticated() {
        final Authentication authentication = getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken)
            && !(authentication instanceof RememberMeAuthenticationToken) && authentication.isAuthenticated();
    }

    @Override
    public Authentication login(String username, String password) throws AuthenticationException, Exception {
        return login(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            authenticationManager = applicationContext.getBean(AuthenticationManager.class);
            logger.info("Using authentication manager {}", authenticationManager);
        } catch (NoSuchBeanDefinitionException ex) {
            logger.warn("No AuthenticationManager found! Some security methods will not be available.");
        }

        try {
            accessDecisionManager = applicationContext.getBean(AccessDecisionManager.class);
            logger.info("Using access decision manager {}", accessDecisionManager);
        } catch (NoSuchBeanDefinitionException ex) {
            accessDecisionManager = null;
            logger.warn("No AccessDecisionManager found! Some security methods will not be available.");
        }

        try {
            rememberMeServices = applicationContext.getBean(RememberMeServices.class);
            logger.info("Using RememberMeServices {}", rememberMeServices);
        } catch (NoSuchBeanDefinitionException ex) {
            rememberMeServices = new NullRememberMeServices();
            logger.info("No RememberMeServices found. Using NullRememberMeServices.");
        }
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Override
    public AccessDecisionManager getAccessDecisionManager() {
        return accessDecisionManager;
    }

    @Override
    public boolean hasAccessDecisionManager() {
        return accessDecisionManager != null;
    }

    @Override
    public boolean hasAuthenticationManager() {
        return authenticationManager != null;
    }

    @Override
    public RememberMeServices getRememberMeServices() {
        return rememberMeServices;
    }

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

    @Override
    public boolean hasAuthorities(String... authorities) {
        for (String authority : authorities) {
            if (!hasAuthority(authority)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        for (String authority : authorities) {
            if (hasAuthority(authority)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAccessToObject(Object securedObject, String... securityConfigurationAttributes) {
        final Authentication authentication = getAuthentication();
        if (getAccessDecisionManager() == null) {
            logger.warn("Access was denied to object because there was no AccessDecisionManager set!");
            return false;
        } else if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        final Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>(
            securityConfigurationAttributes.length);
        for (String securityConfigString : securityConfigurationAttributes) {
            configAttributes.add(new SecurityConfig(securityConfigString));
        }

        try {
            getAccessDecisionManager().decide(authentication, securedObject, configAttributes);
            return true;
        } catch (AccessDeniedException ex) {
            logger.trace("Access denied when accessing {}", securedObject);
            return false;
        } catch (InsufficientAuthenticationException ex) {
            logger.trace("Insufficient authentication when accessing {}", securedObject);
            return false;
        }
    }

    @Override
    public boolean hasAccessToSecuredObject(Object securedObject) {
        final Secured secured = AopUtils.getTargetClass(securedObject).getAnnotation(Secured.class);
        Assert.notNull(secured, "securedObject did not have @Secured annotation");
        return hasAccessToObject(securedObject, secured.value());
    }

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
}
