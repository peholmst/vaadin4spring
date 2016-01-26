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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.managed.internal.SecurityContextVaadinRequestListener;
import org.vaadin.spring.security.managed.internal.VaadinManagedSecurityImpl;

/**
 * Configuration for setting up Vaadin managed Spring Security. See {@link org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity} for details.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
public class VaadinManagedSecurityConfiguration extends AbstractVaadinSecurityConfiguration {

    @Override
    VaadinSecurity vaadinSecurity() {
        return new VaadinManagedSecurityImpl();
    }

    @Bean
    SecurityContextVaadinRequestListener securityContextVaadinRequestListener() {
        return new SecurityContextVaadinRequestListener();
    }

    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    static class GlobalMethodSecurity extends GlobalMethodSecurityConfiguration {

        @Autowired
        ApplicationContext applicationContext;

        @Bean(name = ACCESS_DECISION_MANAGER_BEAN)
        @Override
        protected AccessDecisionManager accessDecisionManager() {
            return super.accessDecisionManager();
        }

        @Bean(name = AUTHENTICATION_MANAGER_BEAN)
        @Override
        protected AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            try {
                applicationContext.getBean(AuthenticationManagerConfigurer.class).configure(auth);
            } catch (Exception ex) {
                throw new IllegalStateException("No AuthenticationManagerConfigurer found. Either define one, or override the GlobalMethodSecurity.configure(AuthenticationManagerBuilder) method.");
            }
        }
    }
}
