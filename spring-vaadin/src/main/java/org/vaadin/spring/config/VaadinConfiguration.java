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

import org.springframework.context.annotation.*;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.internal.ApplicationContextEventBroker;
import org.vaadin.spring.events.internal.ScopedEventBus;
import org.vaadin.spring.internal.VaadinUIScope;
import org.vaadin.spring.navigator.SpringViewProvider;

/**
 * Spring configuration for registering the custom Vaadin {@link org.vaadin.spring.internal.VaadinUIScope scope}
 * and the {@link SpringViewProvider view provider}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
public class VaadinConfiguration {

    @Bean
    static VaadinUIScope uiScope() {
        return new VaadinUIScope();
    }

    @Bean
    SpringViewProvider viewProvider() {
        return new SpringViewProvider();
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
    @EventBusScope(EventScope.SESSION)
    EventBus sessionEventBus() {
        return new ScopedEventBus(EventScope.SESSION, applicationEventBus());
    }

    @Bean
    @Scope(value = "ui", proxyMode = ScopedProxyMode.INTERFACES)
    @Primary
    @EventBusScope(EventScope.UI)
    EventBus uiEventBus() {
        return new ScopedEventBus(EventScope.UI, sessionEventBus());
    }
}
