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
package org.vaadin.spring.samples.mvp.security.config;

import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;

import org.vaadin.spring.samples.mvp.security.dto.FunctionalRole;

/**
 * Builds an in-memory authentication/authorization store
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
enum InMemoryAuthenticationBuilder {

    INSTANCE;

    void build(Environment env, AuthenticationManagerBuilder auth) throws Exception {
        String username = env.getProperty("app.user.name", "admin");
        String password = env.getProperty("app.user.password", "admin");
        InMemoryUserDetailsManagerConfigurer<?> inmem = auth.inMemoryAuthentication();

        // remember to annotate service methods with @org.springframework.security.access.annotation.Secured
        inmem.withUser(username).password(password).authorities(FunctionalRole.ADMIN.getRoleName());

        // other "fake" accounts; for demonstration purposes
        inmem.withUser("user").password("user").authorities(FunctionalRole.PUBLIC.getRoleName());

    }
}
