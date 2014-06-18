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
package org.vaadin.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.internal.ApplicationContextEventBroker;
import org.vaadin.spring.events.internal.ScopedEventBus;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.internal.UIScope;
import org.vaadin.spring.navigator.SpringViewProvider;

/**
 * Spring configuration for registering the custom Vaadin {@link org.vaadin.spring.internal.UIScope scope}
 * and the {@link SpringViewProvider view provider}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
public class VaadinConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    static UIScope uiScope() {
        return new UIScope();
    }

    @Bean
    I18N i18n() {
        return new I18N(applicationContext);
    }

    @Bean
    SpringViewProvider viewProvider() {
        return new SpringViewProvider(applicationContext);
    }

    @Bean
    ApplicationContextEventBroker applicationContextEventBroker() {
        return new ApplicationContextEventBroker(applicationEventBus());
    }

    @Bean
    @EventBusScope(EventScope.APPLICATION)
    EventBus applicationEventBus() {
        return new ScopedEventBus(EventScope.APPLICATION);
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusScope(value = EventScope.SESSION, proxy = true)
    EventBus proxiedSessionEventBus() {
        return sessionEventBus();
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.NO)
    @EventBusScope(EventScope.SESSION)
    EventBus sessionEventBus() {
        return new ScopedEventBus(EventScope.SESSION, applicationEventBus());
    }

    @Bean
    @Scope(value = "ui", proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusScope(value = EventScope.UI, proxy = true)
    EventBus proxiedUiEventBus() {
        return uiEventBus();
    }

    @Bean
    @Scope(value = "ui", proxyMode = ScopedProxyMode.NO)
    @Primary
    @EventBusScope(EventScope.UI)
    EventBus uiEventBus() {
        return new ScopedEventBus(EventScope.UI, sessionEventBus());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
