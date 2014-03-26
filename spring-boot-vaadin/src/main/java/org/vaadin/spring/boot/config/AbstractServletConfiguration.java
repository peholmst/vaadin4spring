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

import com.vaadin.annotations.VaadinServletConfiguration;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.Method;

/**
 * Base class for {@link org.vaadin.spring.boot.config.VaadinServletConfiguration} and {@link org.vaadin.spring.boot.config.TouchKitServletConfiguration}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
abstract class AbstractServletConfiguration implements InitializingBean {

    public static final String DEFAULT_SERVLET_URL_MAPPING = "/*";

    @Autowired
    Environment environment;

    @Autowired
    ApplicationContext applicationContext;

    protected abstract String getServletConfigurationParameterPrefix();

    protected abstract Class<? extends HttpServlet> getServletClass();

    protected abstract Logger getLogger();

    @Override
    public void afterPropertiesSet() throws Exception {
        getLogger().debug("{} initialized", getClass().getName());
    }

    private HttpServlet newServletInstance() {
        try {
            return getServletClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create new servlet instance", e);
        }
    }

    protected abstract String getUrlMapping();

    protected HttpServlet createServlet() {
        HttpServlet servlet;
        try {
            servlet = applicationContext.getBean(getServletClass());
            getLogger().info("Using servlet instance [{}] found in the application context", servlet);
        } catch (NoSuchBeanDefinitionException ex) {
            getLogger().info("Servlet was not found in the application context, using default");
            servlet = newServletInstance();
        }
        return servlet;
    }

    protected ServletRegistrationBean createServletRegistrationBean() {
        getLogger().info("Registering servlet of type [{}]", getServletClass().getCanonicalName());
        final String urlMapping = getUrlMapping();
        getLogger().info("Servlet will be mapped to URL [{}]", urlMapping);
        final HttpServlet servlet = createServlet();
        final ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                servlet, urlMapping);
        addInitParameters(registrationBean);
        return registrationBean;
    }

    private void addInitParameters(ServletRegistrationBean servletRegistrationBean) {
        getLogger().info("Looking for servlet init parameters");
        int count = 0;
        final Method[] methods = VaadinServletConfiguration.class
                .getDeclaredMethods();
        for (Method method : methods) {
            VaadinServletConfiguration.InitParameterName name = method
                    .getAnnotation(VaadinServletConfiguration.InitParameterName.class);
            String propertyValue = environment.getProperty(getServletConfigurationParameterPrefix() + name.value());
            if (propertyValue != null) {
                getLogger().info("Found servlet init parameter [{}] = [{}]", name.value(), propertyValue);
                servletRegistrationBean.addInitParameter(name.value(), propertyValue);
                count++;
            }
        }
        if (count == 0) {
            getLogger().info("Found no servlet init parameters");
        }
    }
}
