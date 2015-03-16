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
package org.vaadin.spring.config;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.request.RequestContextListener;
import org.vaadin.spring.context.VaadinApplicationContext;
import org.vaadin.spring.http.HttpResponseFactory;
import org.vaadin.spring.http.HttpResponseFilter;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.http.VaadinHttpService;

/**
 * Spring configuration that sets up the the extensions for Vaadin Spring.
 * This configuration also includes {@link com.vaadin.spring.annotation.EnableVaadin}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see org.vaadin.spring.annotation.EnableVaadinExtensions
 * @see com.vaadin.spring.annotation.EnableVaadin
 */
@Configuration
@EnableVaadin
public class VaadinExtensionsConfiguration {

    @Bean
    VaadinApplicationContext vaadinApplicationContext() {
        return new VaadinApplicationContext();
    }

    /**
     * Allow access to the current HttpServletRequest
     * through autowiring
     */
    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /**
     * Allow injection of HttpServletResponse
     * Required by {@link HttpResponseFactory}
     */
    @Bean
    HttpResponseFilter httpResponseFilter() {
        return new HttpResponseFilter();
    }

    /**
     * Allow injection of HttpServletResponse
     */
    @Bean
    HttpResponseFactory httpResponseFactory() {
        return new HttpResponseFactory();
    }

    /**
     * Vaadin Http Service
     * Allow access to HttpRequest / HttpResponse
     */
    @Bean
    @Scope(value = org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    HttpService httpService() {
        return new VaadinHttpService();
    }
}
