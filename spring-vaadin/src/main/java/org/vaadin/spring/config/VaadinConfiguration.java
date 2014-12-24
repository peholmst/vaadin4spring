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
import org.springframework.web.context.request.RequestContextListener;
import org.vaadin.spring.context.VaadinApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.internal.ApplicationContextEventBroker;
import org.vaadin.spring.events.internal.ScopedEventBus;
import org.vaadin.spring.http.HttpResponseFactory;
import org.vaadin.spring.http.HttpResponseFilter;
import org.vaadin.spring.http.VaadinHttpService;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.internal.VaadinSessionScope;
import org.vaadin.spring.internal.VaadinUIScope;
import org.vaadin.spring.navigator.SpringViewProvider;

/**
 * Spring configuration for registering the custom Vaadin scopes,
 * the {@link SpringViewProvider view provider} and some other stuff.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
public class VaadinConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    static VaadinSessionScope vaadinSessionScope() {
        return new VaadinSessionScope();
    }

    @Bean
    static VaadinUIScope vaadinUIScope() {
        return new VaadinUIScope();
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Bean
    VaadinApplicationContext vaadinApplicationContext() {
    	return new VaadinApplicationContext();
    }
    
    /**
     * Allow access to the current HttpServletRequest
     * through autowiring
     */
    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    
    /**
     * Allow injection of HttpServletResponse
     * Required by {@link HttpResponseFactory}
     */
    @Bean
    HttpResponseFilter httpResponseFilter() {
        return new HttpResponseFilter();        
    }
    
    /**
     * Allow injection of HttpServletResponse
     */
    @Bean
    HttpResponseFactory httpResponseFactory() {
        return new HttpResponseFactory();
    }
    
    /**
     * Vaadin Http Service
     * Allow access to HttpRequest / HttpResponse
     */
    @Bean
    @Scope(value = org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    VaadinHttpService vaadinHttpService() {
        return new VaadinHttpService();
    }
}
