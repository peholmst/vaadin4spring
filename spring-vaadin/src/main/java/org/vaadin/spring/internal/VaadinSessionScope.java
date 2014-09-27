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

import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of Spring's {@link org.springframework.beans.factory.config.Scope} that binds the beans to the
 * current {@link com.vaadin.server.VaadinSession} (as opposed to the current Servlet session). Registered by default
 * as the scope "{@value #VAADIN_SESSION_SCOPE_NAME}".
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.VaadinSessionScope
 */
public class VaadinSessionScope implements Scope, BeanFactoryPostProcessor {

    public static final String VAADIN_SESSION_SCOPE_NAME = "vaadin-session";
    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinSessionScope.class);

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
        return getVaadinSession().getSession().getId();
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

    private BeanStore getBeanStore() {
        VaadinSession session = getVaadinSession();
        session.lock();
        try {
            BeanStore beanStore = session.getAttribute(BeanStore.class);
            if (beanStore == null) {
                beanStore = new BeanStore(session);
                session.setAttribute(BeanStore.class, beanStore);
            }
            return beanStore;
        } finally {
            session.unlock();
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        LOGGER.debug("Registering VaadinSession scope with bean factory [{}]", configurableListableBeanFactory);
        configurableListableBeanFactory.registerScope(VAADIN_SESSION_SCOPE_NAME, this);
    }

    static class BeanStore implements Serializable, SessionDestroyListener {

        private static final Logger LOGGER = LoggerFactory.getLogger(BeanStore.class);

        private final VaadinSession session;

        private final Map<String, Object> objectMap = new ConcurrentHashMap<>();

        private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

        BeanStore(VaadinSession session) {
            LOGGER.debug("Initializing scope for session [{}]", session);
            this.session = session;
            session.getService().addSessionDestroyListener(this);
        }

        Object get(String s, ObjectFactory<?> objectFactory) {
            LOGGER.trace("Getting bean with name [{}]", s);
            Object bean = objectMap.get(s);
            if (bean == null) {
                LOGGER.trace("Bean with name [{}] not found in store, creating new", s);
                bean = objectFactory.getObject();
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
            LOGGER.debug("Destroying scope for session [{}]", session);
            for (Runnable destructionCallback : destructionCallbacks.values()) {
                destructionCallback.run();
            }
            destructionCallbacks.clear();
            objectMap.clear();
        }

        @Override
        public void sessionDestroy(SessionDestroyEvent event) {
            if (session.equals(event.getSession())) {
                destroy();
                event.getService().removeSessionDestroyListener(this);
            }
        }
    }

}
