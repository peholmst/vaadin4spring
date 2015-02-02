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
package org.vaadin.spring.i18n.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.vaadin.spring.i18n.CompositeMessageSource;
import org.vaadin.spring.i18n.I18N;

/**
 * Configuration class used by {@literal @}EnableVaadinI18N
 * 
 * Spring configuration for the {@link org.vaadin.spring.i18n.CompositeMessageSource}. Please remember to
 * define {@link org.vaadin.spring.i18n.MessageProvider} beans that can serve the message source with messages.
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see org.vaadin.spring.i18n.I18N
 * @see org.vaadin.spring.i18n.CompositeMessageSource
 */
@Configuration
public class VaadinI18NConfiguration implements ApplicationContextAware, InitializingBean {
    
    private ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(context, "Failed to autowire 'ApplicationContext");
    }
    
    @Bean
    I18N i18n() {
        return new I18N(context);
    }

    @Bean
    CompositeMessageSource messageSource() {
        return new CompositeMessageSource(context);
    }
}
