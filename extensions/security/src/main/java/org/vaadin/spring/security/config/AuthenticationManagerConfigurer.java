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

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * Configurer interface used by {@link org.vaadin.spring.security.config.VaadinManagedSecurityConfiguration} to
 * set up the authentication manager. Create a class that implements this interface, annotate it with {@link org.springframework.context.annotation.Configuration}
 * and make sure Spring detects it.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface AuthenticationManagerConfigurer {

    /**
     * Configures the authentication manager using the specified builder.
     *
     * @see org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     */
    void configure(AuthenticationManagerBuilder auth) throws Exception;
}
