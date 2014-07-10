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
package org.vaadin.spring.stuff.i18n.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.stuff.i18n.CompositeMessageSource;

/**
 * Spring configuration for the {@link org.vaadin.spring.stuff.i18n.CompositeMessageSource}. Please remember to
 * define {@link org.vaadin.spring.stuff.i18n.MessageProvider} beans that can serve the message source with messages.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.stuff.i18n.EnableCompositeMessageSource
 */
@Configuration
public class CompositeMessageSourceConfiguration {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    CompositeMessageSource messageSource() {
        return new CompositeMessageSource(applicationContext);
    }
}
