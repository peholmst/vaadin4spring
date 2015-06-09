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
package org.vaadin.spring.security.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.provider.PreAuthorizeViewInstanceAccessControl;
import org.vaadin.spring.security.provider.SecuredViewAccessControl;
import org.vaadin.spring.security.support.VaadinSecurityAwareProcessor;

/**
 * Base class for Vaadin security configurations.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class AbstractVaadinSecurityConfiguration {

    public static final String CURRENT_USER_BEAN = "currentUser";
    public static final String VAADIN_SECURITY_BEAN = "vaadinSecurity";
    public static final String VAADIN_SECURITY_AWARE_PROCESSOR_BEAN = "vaadinSecurityProcessor";
    public static final String AUTHENTICATION_MANAGER_BEAN = "authenticationManager";
    public static final String ACCESS_DECISION_MANAGER_BEAN = "accessDecisionManager";

    @Bean(name = CURRENT_USER_BEAN)
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

    @Bean(name = VAADIN_SECURITY_BEAN)
    abstract VaadinSecurity vaadinSecurity();

    @Bean(name = VAADIN_SECURITY_AWARE_PROCESSOR_BEAN)
    VaadinSecurityAwareProcessor vaadinSecurityProcessor() {
        return new VaadinSecurityAwareProcessor();
    }

    @Bean
    SecuredViewAccessControl securedViewAccessControl() {
        return new SecuredViewAccessControl();
    }

    @Bean
    PreAuthorizeViewInstanceAccessControl preAuthorizeViewInstanceAccessControl() {
        return new PreAuthorizeViewInstanceAccessControl();
    }
}
