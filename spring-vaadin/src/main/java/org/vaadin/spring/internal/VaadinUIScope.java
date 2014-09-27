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

import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of Spring's {@link org.springframework.beans.factory.config.Scope} that binds the UIs and dependent
 * beans to the current {@link com.vaadin.server.VaadinSession} (as opposed to the current Servlet session). Registered by
 * default as the scope "{@value #VAADIN_UI_SCOPE_NAME}".
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.UIScope
 */
public class VaadinUIScope implements Scope, BeanFactoryPostProcessor {

    public static final String VAADIN_UI_SCOPE_NAME = "vaadin-ui";
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinUIScope.class);

    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        return getBeanStore().get(s, objectFactory);
    }

    @Override
    public Object remove(String s) {
        return getBeanStore().remove(s);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {
        getBeanStore().registerDestructionCallback(s, runnable);
    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return getVaadinSession().getSession().getId() + getUIID();
    }

    private VaadinSession getVaadinSession() {
        VaadinSession current = VaadinSession.getCurrent();
        if (current == null) {
            throw new IllegalStateException("No VaadinSession bound to current thread");
        }
        if (current.getState() != VaadinSession.State.OPEN) {
            throw new IllegalStateException("Current VaadinSession is not open");
        }
        return current;
    }

    private UIStore getUIStore() {
        VaadinSession session = getVaadinSession();
        session.lock();
        try {
            UIStore uiStore = session.getAttribute(UIStore.class);
            if (uiStore == null) {
                uiStore = new UIStore();
                session.setAttribute(UIStore.class, uiStore);
            }
            return uiStore;
        } finally {
            session.unlock();
        }
    }

    private BeanStore getBeanStore() {
        return getUIStore().getBeanStore(getUIID());
    }

    private UIID getUIID() {
        final UI currentUI = UI.getCurrent();
        if (currentUI != null && currentUI.getUIId() != -1) {
            return new UIID(currentUI);
        } else {
            UIID currentIdentifier = CurrentInstance.get(UIID.class);
            Assert.notNull(currentIdentifier, String.format("Found no valid %s instance!", UIID.class.getName()));
            return currentIdentifier;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        LOGGER.debug("Registering Vaadin UI scope with bean factory [{}]", configurableListableBeanFactory);
        configurableListableBeanFactory.registerScope(VAADIN_UI_SCOPE_NAME, this);
    }

    static class UIStore implements Serializable {

        private static final Logger LOGGER = LoggerFactory.getLogger(UIStore.class);

        private final Map<UIID, BeanStore> beanStoreMap = new ConcurrentHashMap<>();

        UIStore() {
        }

        BeanStore getBeanStore(UIID uiid) {
            LOGGER.trace("Getting bean store for UI ID [{}]", uiid);
            BeanStore beanStore = beanStoreMap.get(uiid);
            if (beanStore == null) {
                LOGGER.trace("Bean store for UI ID [{}] not found, creating new", uiid);
                beanStore = new BeanStore(uiid, this);
                beanStoreMap.put(uiid, beanStore);
            }
            return beanStore;
        }

        void removeBeanStore(UIID uiid) {
            LOGGER.trace("Removing bean store for UI ID [{}]", uiid);
            beanStoreMap.remove(uiid);
        }
    }

    static class BeanStore implements Serializable, ClientConnector.DetachListener {

        private static final Logger LOGGER = LoggerFactory.getLogger(BeanStore.class);

        private final UIID uiid;

        private final UIStore uiStore;

        private final Map<String, Object> objectMap = new ConcurrentHashMap<>();

        private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

        BeanStore(UIID uiid, UIStore uiStore) {
            LOGGER.debug("Initializing scope for UI ID [{}]", uiid);
            this.uiid = uiid;
            this.uiStore = uiStore;
        }

        Object get(String s, ObjectFactory<?> objectFactory) {
            LOGGER.trace("Getting bean with name [{}]", s);
            Object bean = objectMap.get(s);
            if (bean == null) {
                LOGGER.trace("Bean with name [{}] not found in store, creating new instance", s);
                bean = objectFactory.getObject();
                if (bean instanceof UI) {
                    ((UI) bean).addDetachListener(this);
                }
                if (!(bean instanceof Serializable)) {
                    LOGGER.warn("Storing non-serializable bean [{}] with name [{}]", bean, s);
                }
                objectMap.put(s, bean);
            }
            return bean;
        }

        Object remove(String s) {
            destructionCallbacks.remove(s);
            return objectMap.remove(s);
        }

        void registerDestructionCallback(String s, Runnable runnable) {
            LOGGER.trace("Registering destruction callback for bean with name [{}]", s);
            destructionCallbacks.put(s, runnable);
        }

        void destroy() {
            LOGGER.debug("Destroying scope for UI ID [{}]", uiid);
            for (Runnable destructionCallback : destructionCallbacks.values()) {
                destructionCallback.run();
            }
            destructionCallbacks.clear();
            objectMap.clear();
            uiStore.removeBeanStore(uiid);
        }

        @Override
        public void detach(ClientConnector.DetachEvent event) {
            destroy();
        }
    }

}
