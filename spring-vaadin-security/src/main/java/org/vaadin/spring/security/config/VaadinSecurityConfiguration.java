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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.security.GenericVaadinSecurity;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.provider.SpringSecurityViewProviderAccessDelegate;
import org.vaadin.spring.security.support.VaadinSecurityAwareProcessor;

/**
 * Spring configuration for setting up the Spring Security integration.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see org.vaadin.spring.security.annotation.EnableVaadinSecurity
 */
@Configuration
public class VaadinSecurityConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static final class Beans {
    	
    	public static final String VAADIN_SECURITY					= "vaadinSecurity";
    	public static final String VAADIN_SECURITY_AWARE_PROCESSOR	= "vaadinSecurityProcessor";
    	public static final String CURRENT_USER						= "currentUser";
    	
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
    @DependsOn(value = Beans.VAADIN_SECURITY)
    VaadinSecurityAwareProcessor vaadinSecurityProcessor() {
    	return new VaadinSecurityAwareProcessor();
    }

    @Bean
    @DependsOn(value = {Beans.VAADIN_SECURITY})
    SpringSecurityViewProviderAccessDelegate viewProviderAccessDelegate() {
        return new SpringSecurityViewProviderAccessDelegate(vaadinSecurity(), applicationContext);
    }
}
