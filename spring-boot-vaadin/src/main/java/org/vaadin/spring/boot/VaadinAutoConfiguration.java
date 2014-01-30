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

/**
 *
 * @author petter@vaadin.com
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

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug(getClass().getName() + " has finished running");
        }

        String vaadinServletRegistrationParameterName = "vaadin.mapping" ;

        @Autowired
        Environment environment ;

        @Bean
        ServletRegistrationBean vaadinServlet() {
            logger.debug("registering vaadinServlet()");

            String registrationMappingParameterValue =
                    this.environment.getProperty( vaadinServletRegistrationParameterName, "/ui/*" ) ;

            // TODO Must be possible to parameterize servlet URL mappings and init parameters
            // todo you can easily use application.properties to parameterize this: vaadin.servlet.mapping=/*
            ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                    new SpringAwareVaadinServlet(), registrationMappingParameterValue, "/VAADIN/*");
            registrationBean.addInitParameter("heartbeatInterval", "10"); // In order to test that orphaned UIs are detached properly
            return registrationBean;
        }
    }
}
