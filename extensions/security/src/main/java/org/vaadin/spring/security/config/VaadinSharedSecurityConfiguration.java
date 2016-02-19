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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.shared.*;
import org.vaadin.spring.security.web.DefaultVaadinRedirectStrategy;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.servlet.CustomInitParameterProvider;
import org.vaadin.spring.servlet.SingletonCustomInitParameterProvider;

/**
 * Configuration for setting up Vaadin shared Spring Security. See
 * {@link org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity} for details.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
public class VaadinSharedSecurityConfiguration extends AbstractVaadinSecurityConfiguration {

    public static final String VAADIN_REDIRECT_STRATEGY_BEAN = "vaadinRedirectStrategy";
    public static final String VAADIN_LOGOUT_HANDLER_BEAN = "vaadinLogoutHandler";
    public static final String VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN = "vaadinAuthenticationSuccessHandler";

    @Override
    VaadinSharedSecurity vaadinSecurity() {
        return new DefaultVaadinSharedSecurity();
    }

    @Bean(name = VAADIN_REDIRECT_STRATEGY_BEAN)
    VaadinRedirectStrategy vaadinRedirectStrategy() {
        return new DefaultVaadinRedirectStrategy();
    }

    @Bean(name = VAADIN_LOGOUT_HANDLER_BEAN)
    VaadinLogoutHandler vaadinLogoutHandler(VaadinRedirectStrategy vaadinRedirectStrategy) {
        return new VaadinRedirectLogoutHandler(vaadinRedirectStrategy);
    }

    @Bean(name = VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN)
    VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler(HttpService httpService,
        VaadinRedirectStrategy vaadinRedirectStrategy) {
        return new SavedRequestAwareVaadinAuthenticationSuccessHandler(httpService, vaadinRedirectStrategy, "/");
    }

    @Bean
    CustomInitParameterProvider pushSecurityInterceptorInitParameterProvider() {
        // We're using the class name as a string to avoid ClassNotFoundExceptions when Vaadin Push is not on the
        // classpath.
        return new SingletonCustomInitParameterProvider("org.atmosphere.cpr.AtmosphereInterceptor",
            "org.vaadin.spring.security.shared.PushSecurityInterceptor");
    }
}
