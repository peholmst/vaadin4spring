/*
 * Copyright 2016 The original authors
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

import org.atmosphere.cpr.SessionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configuration for Atmosphere (used by Vaadin websocket push)
 * 
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
@ConditionalOnClass(SessionSupport.class)
public class AtmosphereAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(AtmosphereAutoConfiguration.class);

    @Configuration
    static class AtmosphereSessionSupportConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug("{} initialized", getClass().getName());
        }

        @Bean
        ServletListenerRegistrationBean<SessionSupport> atmosphereSessionSupport() {
            return new ServletListenerRegistrationBean<SessionSupport>(new SessionSupport());
        }
    }
}
