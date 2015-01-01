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
package org.vaadin.spring.boot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.touchkit.servlet.PingServlet;

import javax.servlet.http.HttpServlet;

/**
 * Spring configuration that sets up a PingServlet{@link org.vaadin.spring.touchkit.servlet.PingServlet}
 * needed by some Android offline mode haxies.
 */
@Configuration
public class PingServletConfiguration extends AbstractServletConfiguration {

    private static Logger logger = LoggerFactory.getLogger(PingServletConfiguration.class);

    public static final String SERVLET_CONFIGURATION_PARAMETER_PREFIX = "touchKit.pingservlet.params.";
    public static final String SERVLET_URL_MAPPING_PARAMETER_NAME = "touchKit.pingservlet.urlMapping";

    @Override
    protected String getServletConfigurationParameterPrefix() {
        return SERVLET_CONFIGURATION_PARAMETER_PREFIX;
    }

    @Override
    protected Class<? extends HttpServlet> getServletClass() {
        return PingServlet.class;
    }

    @Override
    protected HttpServlet createServlet() {
        return new PingServlet();
    }


    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getUrlMapping() {
        return environment.getProperty(SERVLET_URL_MAPPING_PARAMETER_NAME, "/PING");
    }

    @Bean
    ServletRegistrationBean pingServletRegistration() {
        return createServletRegistrationBean();
    }
}
