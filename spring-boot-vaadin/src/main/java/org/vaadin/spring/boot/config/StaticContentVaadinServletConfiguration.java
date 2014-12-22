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

import javax.servlet.http.HttpServlet;

/**
 * Spring configuration that sets up a {@link org.vaadin.spring.boot.config.StaticContentServlet} to serve static content from /VAADIN/*.
 * If you want to customize the servlet, extend it and make it available as a Spring bean.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
public class StaticContentVaadinServletConfiguration extends AbstractServletConfiguration {

    private static Logger logger = LoggerFactory.getLogger(StaticContentVaadinServletConfiguration.class);

    /**
     * Prefix to be used for all Spring environment properties that configure the static content Vaadin servlet.
     * The full format of the environment property name is {@code [prefix][initParameter]} where {@code [prefix]}
     * is <code>{@value}</code> and {@code initParameter} is the name of one of the parameters defined in {@link com.vaadin.annotations.VaadinServletConfiguration}.
     * <p>
     * For example, to change the production mode of the servlet, a property named <code>{@value}productionMode</code> would
     * be used.
     *
     * @see org.springframework.core.env.Environment
     */
    public static final String SERVLET_CONFIGURATION_PARAMETER_PREFIX = "vaadin.static.servlet.params.";

    @Override
    protected String getServletConfigurationParameterPrefix() {
        return SERVLET_CONFIGURATION_PARAMETER_PREFIX;
    }

    @Override
    protected Class<? extends HttpServlet> getServletClass() {
        return StaticContentServlet.class;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getUrlMapping() {
        return "/VAADIN/*";
    }

    @Bean
    ServletRegistrationBean staticContentVaadinServletRegistration() {
        return createServletRegistrationBean();
    }
}
