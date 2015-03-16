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
package org.vaadin.spring.events.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusAware;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the corresponding EventBus to beans that
 * implement the one of the {@link org.vaadin.spring.events.EventBusAware} interfaces
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinEventBusAwareProcessor implements ApplicationContextAware, BeanPostProcessor {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
        AccessControlContext acc = null;

        if (System.getSecurityManager() != null && (bean instanceof EventBusAware)) {
            acc = this.applicationContext.getBeanFactory().getAccessControlContext();
        }

        if (acc != null) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {

                @Override
                public Object run() {
                    invokeAwareInterfaces(bean);
                    return null;
                }

            }, acc);
        } else {
            invokeAwareInterfaces(bean);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void invokeAwareInterfaces(Object bean) {
        if (bean instanceof EventBusAware) {

            if (bean instanceof EventBusAware.ApplicationEventBusAware) {
                ((EventBusAware.ApplicationEventBusAware) bean).setApplicationEventBus(this.applicationContext.getBean(EventBus.ApplicationEventBus.class));
            }
            if (bean instanceof EventBusAware.SessionEventBusAware) {
                ((EventBusAware.SessionEventBusAware) bean).setSessionEventBus(this.applicationContext.getBean(EventBus.SessionEventBus.class));
            }
            if (bean instanceof EventBusAware.UIEventBusAware) {
                ((EventBusAware.UIEventBusAware) bean).setUIEventBus(this.applicationContext.getBean(EventBus.UIEventBus.class));
            }
            if (bean instanceof EventBusAware.ViewEventBusAware) {
                ((EventBusAware.ViewEventBusAware) bean).setViewEventBus(this.applicationContext.getBean(EventBus.ViewEventBus.class));
            }

        }
    }
}
