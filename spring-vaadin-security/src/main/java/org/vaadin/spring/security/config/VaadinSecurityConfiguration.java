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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.security.GenericVaadinSecurity;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.provider.PreAuthorizeViewProviderAccessDelegate;
import org.vaadin.spring.security.provider.SecuredViewProviderAccessDelegate;
import org.vaadin.spring.security.support.VaadinSecurityAwareProcessor;

/**
 * Spring configuration for setting up the Spring Security integration.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see org.vaadin.spring.security.annotation.EnableVaadinSecurity
 */
@Configuration
public class VaadinSecurityConfiguration {

    public static final class Beans {

        public static final String VAADIN_SECURITY                  = "vaadinSecurity";
        public static final String VAADIN_SECURITY_AWARE_PROCESSOR  = "vaadinSecurityProcessor";
        public static final String CURRENT_USER                     = "currentUser";
        public static final String ACCESS_DECISION_MANAGER          = "accessDecisionManager";
        public static final String AUTHENTICATION_MANAGER           = "authenticationManager";

    }

    @Bean(name = Beans.CURRENT_USER)
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

    @Bean(name = Beans.VAADIN_SECURITY)
    VaadinSecurity vaadinSecurity() {
        return new GenericVaadinSecurity();
    }

    @Bean(name = Beans.VAADIN_SECURITY_AWARE_PROCESSOR)
    VaadinSecurityAwareProcessor vaadinSecurityProcessor() {
        return new VaadinSecurityAwareProcessor();
    }

    @Bean
    SecuredViewProviderAccessDelegate securedViewProviderAccessDelegate() {
        return new SecuredViewProviderAccessDelegate();
    }
    
    @Bean
    PreAuthorizeViewProviderAccessDelegate preAuthorizeViewProviderAccessDelegate() {
        return new PreAuthorizeViewProviderAccessDelegate();
    }

    /**
     * Enable GlobalMethod Security
     *  - Enable @Secured annotation
     *  - Enable @PreAuthorize annotation
     *  - Enable @PostAuthorize annotation
     *  
     * Enable default accessDecisionManager, required for Pre/PostAuthrorize annotations
     * Pre/PostAuthorize requires {@link org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter}
     * 
     * If custom AccessDecisionManager is required, override bean definition
     * by providing custom accessDecisionManager with id={@link VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER}
     * or by providing annotation: &#64;Bean(name = VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER)
     * 
     * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
     * @see GlobalMethodSecurityConfiguration
     */
    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    static class GlobalMethodSecurity extends GlobalMethodSecurityConfiguration {

        @Bean(name = VaadinSecurityConfiguration.Beans.ACCESS_DECISION_MANAGER)
        @Override
        protected AccessDecisionManager accessDecisionManager() {
            return super.accessDecisionManager();
        }

    }

}
