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
package org.vaadin.spring.boot;

import com.vaadin.annotations.VaadinServletConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.internal.SpringAwareVaadinServlet;
import org.vaadin.spring.internal.VaadinUIScope;

import java.lang.reflect.Method;

/**
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
@ConditionalOnClass(VaadinUIScope.class)
public class VaadinAutoConfiguration {

    private static Log logger = LogFactory.getLog(VaadinAutoConfiguration.class);

    /**
     * If the outer {@code \@Configuration} class is enabled (e.g., the
     * {@link org.vaadin.spring.internal.VaadinUIScope UI scope} implementation is on the CLASSPATH),
     * _then_ we let Spring import the configuration class.
     */
    @Configuration
    @EnableVaadin
    static class EnableVaadinConfiguration implements InitializingBean {

        /**
         * Prefix to be used for all Spring environment properties that configure the Vaadin servlet.
         * The full format of the environment property name is {@code [prefix][initParameter]} where {@code [prefix]}
         * is <code>{@value}</code> and {@code initParameter} is the name of one of the parameters defined in {@link VaadinServletConfiguration}.
         * <p/>
         * For example, to change the production mode of the servlet, a property named <code>{@value}productionMode</code> would
         * be used.
         *
         * @see Environment
         */
        public static final String SERVLET_CONFIGURATION_PARAMETER_PREFIX = "vaadin.servlet.params.";
        /**
         * Name of the Spring environment property that contains the URL mapping of the Vaadin servlet. By default, this mapping is {@code /*}.
         */
        public static final String SERVLET_URL_MAPPING_PARAMETER_NAME = "vaadin.servlet.urlMapping";
        @Autowired
        Environment environment;

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug(getClass().getName() + " has finished running");
        }

        @Bean
        ServletRegistrationBean vaadinServlet() {
            logger.debug("Registering Vaadin servlet");
            final String urlMapping = this.environment.getProperty(SERVLET_URL_MAPPING_PARAMETER_NAME, "/*");
            logger.debug("Vaadin Servlet will be mapped to URL [" + urlMapping + "]");
            final ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                    new SpringAwareVaadinServlet(), urlMapping, "/VAADIN/*");
            addInitParameters(registrationBean);
            return registrationBean;
        }

        private void addInitParameters(ServletRegistrationBean servletRegistrationBean) {
            logger.debug("Looking for servlet init parameters");
            final Method[] methods = VaadinServletConfiguration.class
                    .getDeclaredMethods();
            for (Method method : methods) {
                VaadinServletConfiguration.InitParameterName name = method
                        .getAnnotation(VaadinServletConfiguration.InitParameterName.class);
                String propertyValue = environment.getProperty(SERVLET_CONFIGURATION_PARAMETER_PREFIX + name.value());
                if (propertyValue != null) {
                    logger.debug(String.format("Found servlet init parameter [%s] = [%s]", name.value(), propertyValue));
                    servletRegistrationBean.addInitParameter(name.value(), propertyValue);
                }
            }
        }
    }
}
