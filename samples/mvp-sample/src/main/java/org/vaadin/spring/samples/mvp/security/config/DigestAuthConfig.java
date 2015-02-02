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

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

/**
 * Encapsulates configuration details for implementing <a href="http://en.wikipedia.org/wiki/Digest_access_authentication">digest authentication</a>.
 * May be influenced by <code>app.security.digest.*</code> properties declared in <code>application.yml</code>.
 * (EXPERIMENTAL)
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@Configuration
class DigestAuthConfig extends WebSecurityConfigurerAdapter {

    private static final String DEFAULT_DIGEST_KEY = "appKey";
    private static final String DEFAULT_DIGEST_REALM = "appRealm";
    private static final String DEFAULT_DIGEST_NONCE_VALIDITY = "10";

    @Inject
    Environment env;

    // @see http://docs.spring.io/spring-security/site/docs/4.0.0.CI-SNAPSHOT/reference/htmlsingle/#digest-processing-filter
    @Bean
    DigestAuthenticationFilter digestAuthenticationFilter() throws Exception {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());
        return digestAuthenticationFilter;
    }

    @Bean
    public DigestAuthenticationEntryPoint digestEntryPoint() {
        DigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new DigestAuthenticationEntryPoint();
        digestAuthenticationEntryPoint.setKey(env.getProperty("app.security.digest.key", DEFAULT_DIGEST_KEY));
        digestAuthenticationEntryPoint.setRealmName(env.getProperty("app.security.digest.realm", DEFAULT_DIGEST_REALM));
        digestAuthenticationEntryPoint.setNonceValiditySeconds(Integer.valueOf(env.getProperty("app.security.digest.validity", DEFAULT_DIGEST_NONCE_VALIDITY)));
        return digestAuthenticationEntryPoint;
    }
}
