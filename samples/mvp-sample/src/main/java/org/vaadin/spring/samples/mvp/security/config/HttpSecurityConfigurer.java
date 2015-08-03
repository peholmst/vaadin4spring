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

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configures variants of {@link HttpSecurity} influenced by
 * <code>app.security.scheme</code> found in <code>application.yml</code>.
 * Consult <a href="http://docs.spring.io/autorepo/docs/spring-security/4.0.0.CI-SNAPSHOT/reference/htmlsingle/#multiple-httpsecurity">Multiple Security</a>.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
enum HttpSecurityConfigurer {

    INSTANCE;

    void configure(Environment env, ApplicationContext context, HttpSecurity http) throws Exception {
        // all requests are authenticated
        http
        .authorizeRequests()
        .antMatchers("/VAADIN/**", "/PUSH/**", "/UIDL/**", "/login", "/login/**").permitAll()
        .antMatchers("/**").fullyAuthenticated()
        .and()
        // Vaadin chokes if this filter is enabled, disable it!
        .csrf().disable();
        

        // have UI peacefully coexist with Apache CXF web-services
        String id = env.getProperty("app.security.scheme", Scheme.BASIC.id());
        Scheme scheme = Scheme.fromValue(id);
        switch(scheme) {
            case FORM:
                http
                .formLogin()
                  .failureUrl("/login?error")
                  .defaultSuccessUrl("/ui")
                  .permitAll()
                .and()
                  .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .permitAll();
                break;
            case BASIC:
                http.httpBasic();
                break;
            case DIGEST:
                // @see http://java.dzone.com/articles/basic-and-digest
                http.httpBasic();
                http.addFilterAfter(context.getBean(DigestAuthenticationFilter.class), BasicAuthenticationFilter.class);
                break;
        }

        // TODO plumb custom HTTP 403 and 404 pages
        /* http.exceptionHandling().accessDeniedPage("/access?error"); */
    }
}
