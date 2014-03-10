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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.vaadin.spring.servlet.SpringAwareVaadinServlet;

/**
 * Spring configuration that sets up a {@link org.vaadin.spring.servlet.SpringAwareVaadinServlet}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
public class VaadinServletConfiguration extends AbstractServletConfiguration {

    /**
     * Prefix to be used for all Spring environment properties that configure the Vaadin servlet.
     * The full format of the environment property name is {@code [prefix][initParameter]} where {@code [prefix]}
     * is <code>{@value}</code> and {@code initParameter} is the name of one of the parameters defined in {@link com.vaadin.annotations.VaadinServletConfiguration}.
     * <p/>
     * For example, to change the production mode of the servlet, a property named <code>{@value}productionMode</code> would
     * be used.
     *
     * @see org.springframework.core.env.Environment
     */
    public static final String SERVLET_CONFIGURATION_PARAMETER_PREFIX = "vaadin.servlet.params.";
    /**
     * Name of the Spring environment property that contains the URL mapping of the Vaadin servlet. By default, this mapping is {@code /*}.
     */
    public static final String SERVLET_URL_MAPPING_PARAMETER_NAME = "vaadin.servlet.urlMapping";
    public static final String DEFAULT_SERVLET_URL_MAPPING = "/*";

    @Autowired
    Environment environment;

    @Override
    protected Environment getEnvironment() {
        return environment;
    }

    @Bean
    ServletRegistrationBean vaadinServlet() {
        logger.debug("Registering Vaadin servlet");
        final String urlMapping = this.environment.getProperty(SERVLET_URL_MAPPING_PARAMETER_NAME, DEFAULT_SERVLET_URL_MAPPING);
        logger.debug("Vaadin Servlet will be mapped to URL [" + urlMapping + "]");
        final ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                new SpringAwareVaadinServlet(), urlMapping);
        addInitParameters(registrationBean);
        return registrationBean;
    }

    @Override
    protected String getServletConfigurationParameterPrefix() {
        return SERVLET_CONFIGURATION_PARAMETER_PREFIX;
    }
}
