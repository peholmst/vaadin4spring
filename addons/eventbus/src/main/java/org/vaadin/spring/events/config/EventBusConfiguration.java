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

import com.vaadin.spring.internal.UIScopeImpl;
import com.vaadin.spring.internal.VaadinSessionScope;
import com.vaadin.spring.internal.ViewScopeImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusProxy;
import org.vaadin.spring.events.internal.ScopedEventBus;
import org.vaadin.spring.events.support.VaadinEventBusAwareProcessor;

/**
 * Configuration class to configure the Spring Vaadin Eventbus
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
@Configuration
public class EventBusConfiguration {

    @Bean
    VaadinEventBusAwareProcessor vaadinEventBusProcessor() {
        return new VaadinEventBusAwareProcessor();
    }

    @Bean
    EventBus.ApplicationEventBus applicationEventBus() {
        return new ScopedEventBus.DefaultApplicationEventBus();
    }

    @Bean
    @Scope(value = VaadinSessionScope.VAADIN_SESSION_SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusProxy
    EventBus.SessionEventBus proxiedSessionEventBus() {
        return sessionEventBus();
    }

    @Bean
    @Scope(value = VaadinSessionScope.VAADIN_SESSION_SCOPE_NAME, proxyMode = ScopedProxyMode.NO)
    @Primary
    EventBus.SessionEventBus sessionEventBus() {
        return new ScopedEventBus.DefaultSessionEventBus(applicationEventBus());
    }

    @Bean
    @Scope(value = UIScopeImpl.VAADIN_UI_SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusProxy
    EventBus.UIEventBus proxiedUiEventBus() {
        return uiEventBus();
    }

    @Bean
    @Scope(value = UIScopeImpl.VAADIN_UI_SCOPE_NAME, proxyMode = ScopedProxyMode.NO)
    @Primary
    EventBus.UIEventBus uiEventBus() {
        return new ScopedEventBus.DefaultUIEventBus(sessionEventBus());
    }

    @Bean
    @Scope(value = ViewScopeImpl.VAADIN_VIEW_SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
    @EventBusProxy
    EventBus.ViewEventBus proxiedViewEventBus() {
        return viewEventBus();
    }

    @Bean
    @Scope(value = ViewScopeImpl.VAADIN_VIEW_SCOPE_NAME, proxyMode = ScopedProxyMode.NO)
    @Primary
    EventBus.ViewEventBus viewEventBus() {
        return new ScopedEventBus.DefualtViewEventBus(uiEventBus());
    }
}
