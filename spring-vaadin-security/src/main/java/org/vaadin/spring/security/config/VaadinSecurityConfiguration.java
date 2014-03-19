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
package org.vaadin.spring.security.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.security.Security;
import org.vaadin.spring.security.SpringSecurityViewProviderAccessDelegate;

/**
 * Spring configuration for setting up the Spring Security integration.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.security.EnableVaadinSecurity
 */
@Configuration
public class VaadinSecurityConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    Authentication currentUser() {
        return ProxyFactory.getProxy(Authentication.class, new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                if (authentication == null) {
                    throw new AuthenticationCredentialsNotFoundException("No authentication found in current security context");
                }
                return invocation.getMethod().invoke(authentication, invocation.getArguments());
            }
        });
    }

    @Bean
    Security security() {
        AuthenticationManager authenticationManager;
        try {
            authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        } catch (NoSuchBeanDefinitionException ex) {
            authenticationManager = null;
        }

        AccessDecisionManager accessDecisionManager;
        try {
            accessDecisionManager = applicationContext.getBean(AccessDecisionManager.class);
        } catch (NoSuchBeanDefinitionException ex) {
            accessDecisionManager = null;
        }
        return new Security(authenticationManager, accessDecisionManager);
    }

    @Bean
    SpringSecurityViewProviderAccessDelegate viewProviderAccessDelegate() {
        return new SpringSecurityViewProviderAccessDelegate(security(), applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
