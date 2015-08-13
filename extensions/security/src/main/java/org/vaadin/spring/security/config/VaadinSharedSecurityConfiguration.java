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
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.internal.VaadinSharedSecurity;
import org.vaadin.spring.security.web.VaadinDefaultRedirectStrategy;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.security.web.authentication.SavedRequestAwareVaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinLogoutHandler;
import org.vaadin.spring.security.web.authentication.VaadinRedirectLogoutHandler;

/**
 * Configuration for setting up Vaadin shared Spring Security. See {@link org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity} for details.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
public class VaadinSharedSecurityConfiguration extends AbstractVaadinSecurityConfiguration {

    public static final String VAADIN_REDIRECT_STRATEGY_BEAN = "vaadinRedirectStrategy";
    public static final String VAADIN_LOGOUT_HANDLER_BEAN = "vaadinLogoutHandler";
    public static final String VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN = "vaadinAuthenticationSuccessHandler";

    @Override
    VaadinSecurity vaadinSecurity() {
        return new VaadinSharedSecurity();
    }

    @Bean(name = VAADIN_REDIRECT_STRATEGY_BEAN)
    VaadinRedirectStrategy vaadinRedirectStrategy() {
        return new VaadinDefaultRedirectStrategy();
    }

    @Bean(name = VAADIN_LOGOUT_HANDLER_BEAN)
    VaadinLogoutHandler vaadinLogoutHandler(VaadinRedirectStrategy vaadinRedirectStrategy) {
        return new VaadinRedirectLogoutHandler(vaadinRedirectStrategy);
    }

    @Bean(name = VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN)
    VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler(HttpService httpService, VaadinRedirectStrategy vaadinRedirectStrategy) {
        return new SavedRequestAwareVaadinAuthenticationSuccessHandler(httpService, vaadinRedirectStrategy, "/");
    }
}
