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
package org.vaadin.spring.events.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusScope;
import org.vaadin.spring.events.internal.ScopedEventBus;
import org.vaadin.spring.events.support.VaadinEventBusAwareProcessor;
import org.vaadin.spring.internal.VaadinSessionScope;
import org.vaadin.spring.internal.VaadinUIScope;
import org.vaadin.spring.navigator.internal.VaadinViewScope;

/**
 * Configuration class to configure the Spring Vaadin Eventbus
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
@Configuration
public class VaadinEventbusConfiguration {

    @Bean
    VaadinEventBusAwareProcessor vaadinEventBusProcessor() {
        return new VaadinEventBusAwareProcessor();
    }

    @Bean
    @EventBusScope(EventScope.APPLICATION)
    EventBus applicationEventBus() {
        return new ScopedEventBus(EventScope.APPLICATION);
    }

    @Bean
    @Scope(value = VaadinSessionScope.VAADIN_SESSION_SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusScope(value = EventScope.SESSION, proxy = true)
    EventBus proxiedSessionEventBus() {
        return sessionEventBus();
    }

    @Bean
    @Scope(value = VaadinSessionScope.VAADIN_SESSION_SCOPE_NAME, proxyMode = ScopedProxyMode.NO)
    @EventBusScope(EventScope.SESSION)
    EventBus sessionEventBus() {
        return new ScopedEventBus(EventScope.SESSION, applicationEventBus());
    }

    @Bean
    @Scope(value = VaadinUIScope.VAADIN_UI_SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusScope(value = EventScope.UI, proxy = true)
    EventBus proxiedUiEventBus() {
        return uiEventBus();
    }

    @Bean
    @Scope(value = VaadinUIScope.VAADIN_UI_SCOPE_NAME, proxyMode = ScopedProxyMode.NO)
    @Primary
    @EventBusScope(EventScope.UI)
    EventBus uiEventBus() {
        return new ScopedEventBus(EventScope.UI, sessionEventBus());
    }

    @Bean
    @Scope(value = VaadinViewScope.VAADIN_VIEW_SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusScope(EventScope.VIEW)
    EventBus proxiedViewEventBus() {
        return viewEventBus();
    }

    @Bean
    @Scope(value = VaadinViewScope.VAADIN_VIEW_SCOPE_NAME, proxyMode = ScopedProxyMode.NO)
    @EventBusScope(EventScope.VIEW)
    EventBus viewEventBus() {
        return new ScopedEventBus(EventScope.VIEW, uiEventBus());
    }
}
