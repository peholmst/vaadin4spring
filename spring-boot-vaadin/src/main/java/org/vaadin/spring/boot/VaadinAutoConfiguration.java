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

import com.vaadin.server.Constants;
import com.vaadin.server.VaadinServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.VaadinUI;

/**
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
@ConditionalOnClass(VaadinUI.class)
public class VaadinAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(VaadinAutoConfiguration.class);

    @Configuration
    @EnableVaadin
    static class EnableVaadinConfiguration implements InitializingBean {
        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug("{} initialized", getClass().getName());
        }

        @Bean
        ServletRegistrationBean vaadinStaticServlet() {
            logger.info("Registering servlet for serving static Vaadin content");
            final ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                    new VaadinServlet(), "/VAADIN/*");
            // TODO Make this configurable as well
            registrationBean.addInitParameter(Constants.SERVLET_PARAMETER_PRODUCTION_MODE, "true");
            return registrationBean;
        }
    }

    @Configuration
    @EnableVaadinServlet
    @ConditionalOnMissingClass(name = "org.vaadin.spring.touchkit.TouchKitUI")
    static class EnableVaadinServletConfiguration implements InitializingBean {
        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug("{} initialized", getClass().getName());
        }
    }

}
