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
package org.vaadin.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.EnableVaadinExtensions;
import org.vaadin.spring.config.VaadinExtensionsConfiguration;
import org.vaadin.spring.i18n.I18N;

/**
 * Auto configuration for Vaadin4Spring core extensions.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.annotation.EnableVaadinExtensions
 */
@Configuration
@ConditionalOnClass(VaadinExtensionsConfiguration.class)
public class ExtensionsAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ExtensionsAutoConfiguration.class);

    @Configuration
    @EnableVaadinExtensions
    static class EnableExtensionsConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug("{} initialized", getClass().getName());
        }
    }
}
