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
package org.vaadin.spring.internal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Implementation of Spring's {@link org.springframework.beans.factory.config.Scope} contract.
 * Registered by default as the scope {@code ui}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.UIScope
 */
public class UIScope implements Scope, BeanFactoryPostProcessor {

    public static final String UI_SCOPE_NAME = "ui";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UIStore getUIStore() {
        final String attributeName = UIStore.class.getCanonicalName();
        UIStore uiStore = (UIStore) RequestContextHolder.currentRequestAttributes().getAttribute(attributeName, RequestAttributes.SCOPE_SESSION);
        if (uiStore == null) {
            uiStore = new UIStore();
            RequestContextHolder.currentRequestAttributes().setAttribute(attributeName, uiStore, RequestAttributes.SCOPE_SESSION);
        }
        return uiStore;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        return getUIStore().get(name, objectFactory);
    }

    @Override
    public Object remove(String name) {
        return getUIStore().remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        getUIStore().registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return getUIStore().getConversationId();
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.debug("Registering UI scope with beanFactory [{}]", beanFactory);
        beanFactory.registerScope(UI_SCOPE_NAME, this);
    }
}
