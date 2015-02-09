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
package org.vaadin.spring.security.support;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.vaadin.spring.security.navigator.provider.SecuredNavigatorProvider;
import org.vaadin.spring.security.navigator.provider.SecuredNavigatorProviderAware;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the {@link SecuredNavigatorProvider} to beans that
 * implement the {@link org.vaadin.spring.security.navigator.provider.SecuredNavigatorProviderAware} interface
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @since 02.06.2014
 * @see org.vaadin.spring.security.VaadinSecurityAware
 */
public class SecuredNavigatorProviderAwareProcessor implements ApplicationContextAware, BeanPostProcessor {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

        AccessControlContext acc = null;

        if ( System.getSecurityManager() != null && (bean instanceof SecuredNavigatorProviderAware) ) {
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
        }
        else {
            invokeAwareInterfaces(bean);
        }

        return bean;

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void invokeAwareInterfaces(Object bean) {

        if ( bean instanceof Aware ) {

            if ( bean instanceof SecuredNavigatorProviderAware ) {
                ((SecuredNavigatorProviderAware) bean).setSecuredNavigatorProvider(this.applicationContext.getBean(SecuredNavigatorProvider.class));
            }
        }

    }

}
