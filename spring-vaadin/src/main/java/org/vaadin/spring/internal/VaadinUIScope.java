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
import com.vaadin.server.ServiceDestroyEvent;
import com.vaadin.server.ServiceDestroyListener;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
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
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of Spring's {@link org.springframework.beans.factory.config.Scope} that binds the UIs and dependent
 * beans to the current {@link com.vaadin.server.VaadinSession} (as opposed to the current Servlet session). Registered by
 * default as the scope "{@value #VAADIN_UI_SCOPE_NAME}".
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.VaadinUIScope
 */
public class VaadinUIScope implements Scope, BeanFactoryPostProcessor {

    public static final String VAADIN_UI_SCOPE_NAME = "vaadin-ui";
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinUIScope.class);

    private static BeanStoreRetrievalStrategy beanStoreRetrievalStrategy = new VaadinSessionBeanStoreRetrievalStrategy();

    /**
     * Sets the {@link BeanStoreRetrievalStrategy} to use.
     */
    public static synchronized void setBeanStoreRetrievalStrategy(BeanStoreRetrievalStrategy beanStoreRetrievalStrategy) {
        if (beanStoreRetrievalStrategy == null) {
            beanStoreRetrievalStrategy = new VaadinSessionBeanStoreRetrievalStrategy();
        }
        VaadinUIScope.beanStoreRetrievalStrategy = beanStoreRetrievalStrategy;
    }

    /**
     * Returns the {@link BeanStoreRetrievalStrategy} to use.
     * By default, {@link org.vaadin.spring.internal.VaadinUIScope.VaadinSessionBeanStoreRetrievalStrategy} is used.
     */
    public static synchronized BeanStoreRetrievalStrategy getBeanStoreRetrievalStrategy() {
        return beanStoreRetrievalStrategy;
    }

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
        return getBeanStoreRetrievalStrategy().getConversationId();
    }

    private BeanStore getBeanStore() {
        return getBeanStoreRetrievalStrategy().getBeanStore();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        LOGGER.debug("Registering Vaadin UI scope with bean factory [{}]", configurableListableBeanFactory);
        configurableListableBeanFactory.registerScope(VAADIN_UI_SCOPE_NAME, this);
    }

    /**
     * Implementation of {@link BeanStoreRetrievalStrategy} that
     * stores the {@link BeanStore} in the current {@link com.vaadin.server.VaadinSession}.
     */
    public static class VaadinSessionBeanStoreRetrievalStrategy implements BeanStoreRetrievalStrategy {

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
            final VaadinSession session = getVaadinSession();
            session.lock();
            try {
                UIStore uiStore = session.getAttribute(UIStore.class);
                if (uiStore == null) {
                    uiStore = new UIStore();
                    session.setAttribute(UIStore.class, uiStore);
                    final UIStore theStore = uiStore;
                    session.getService().addSessionDestroyListener(new SessionDestroyListener() {
                        @Override
                        public void sessionDestroy(SessionDestroyEvent event) {
                            if (event.getSession().equals(session)) {
                                try {
                                    LOGGER.debug("Vaadin session {} has been destroyed, destroying UI store {}", session, theStore);
                                    theStore.destroy();
                                } finally {
                                    event.getService().removeSessionDestroyListener(this);
                                }
                            }
                        }
                    });
                    session.getService().addServiceDestroyListener(new ServiceDestroyListener() {
                        @Override
                        public void serviceDestroy(ServiceDestroyEvent event) {
                            try {
                                LOGGER.debug("Vaadin service has been destroyed, destroying UI store {}", theStore);
                                theStore.destroy();
                            } finally {
                                event.getSource().removeServiceDestroyListener(this);
                            }
                        }
                    });
                }
                return uiStore;
            } finally {
                session.unlock();
            }
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
        public BeanStore getBeanStore() {
            return getUIStore().getBeanStore(getUIID());
        }

        @Override
        public String getConversationId() {
            return getVaadinSession().getSession().getId() + getUIID();
        }
    }

    static class UIStore implements Serializable {

        private static final Logger LOGGER = LoggerFactory.getLogger(UIStore.class);

        private final Map<UIID, BeanStore> beanStoreMap = new ConcurrentHashMap<UIID, BeanStore>();

        UIStore() {
        }

        BeanStore getBeanStore(final UIID uiid) {
            LOGGER.trace("Getting bean store for UI ID [{}]", uiid);
            BeanStore beanStore = beanStoreMap.get(uiid);
            if (beanStore == null) {
                LOGGER.trace("Bean store for UI ID [{}] not found, creating new", uiid);
                beanStore = new UIBeanStore("UI " + uiid, new BeanStore.DestructionCallback() {
                    @Override
                    public void beanStoreDestoyed(BeanStore beanStore) {
                        removeBeanStore(uiid);
                    }
                });
                beanStoreMap.put(uiid, beanStore);
            }
            return beanStore;
        }

        void removeBeanStore(UIID uiid) {
            LOGGER.trace("Removing bean store for UI ID [{}]", uiid);
            beanStoreMap.remove(uiid);
        }

        void destroy() {
            for (BeanStore beanStore : new HashSet<BeanStore>(beanStoreMap.values())) {
                beanStore.destroy();
            }
            Assert.isTrue(beanStoreMap.isEmpty(), "beanStore should have been emptied by the destruction callbacks");
        }
    }

    static class UIBeanStore extends BeanStore implements ClientConnector.DetachListener {

        public UIBeanStore(String identification, DestructionCallback destructionCallback) {
            super(identification, destructionCallback);
        }

        public UIBeanStore(String identification) {
            super(identification);
        }

        @Override
        protected Object create(String s, ObjectFactory<?> objectFactory) {
            Object bean = super.create(s, objectFactory);
            if (bean instanceof UI) {
                ((UI) bean).addDetachListener(this);
            }
            return bean;
        }

        @Override
        public void detach(ClientConnector.DetachEvent event) {
            destroy();
        }
    }
}
