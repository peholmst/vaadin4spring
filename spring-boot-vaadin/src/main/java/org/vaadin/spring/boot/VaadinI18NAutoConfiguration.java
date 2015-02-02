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
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.i18n.annotation.EnableVaadinI18N;

/**
 * Autoconfiguration Vaadin I18N
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see org.vaadin.spring.i18n.annotation.EnableVaadinI18N
 */
@Configuration
@ConditionalOnClass(I18N.class)
public class VaadinI18NAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(VaadinI18NAutoConfiguration.class);
    
    @Configuration
    @EnableVaadinI18N
    static class EnableVaadinI18NConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug("{} initialized", getClass().getName());
        }
    }
}
