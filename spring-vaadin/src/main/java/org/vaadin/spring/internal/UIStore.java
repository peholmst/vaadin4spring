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
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serializable storage for all UI scoped beans. The idea is to have one {@code UIStore} stored in each session.
 * Thus, when the session is deserialized, all UI scoped beans and destruction callbacks should also be deserialized.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Deprecated
class UIStore implements Serializable, ClientConnector.DetachListener, HttpSessionBindingListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<UIID, Map<String, Object>> objectMap = new ConcurrentHashMap<>();
    private final Map<UIID, Map<String, Runnable>> destructionCallbackMap = new ConcurrentHashMap<>();

    public UIID currentUIID() {
        final UI currentUI = UI.getCurrent();
        if (currentUI != null && currentUI.getUIId() != -1) {
            return new UIID(currentUI);
        } else {
            UIID currentIdentifier = CurrentInstance.get(UIID.class);
            Assert.notNull(currentIdentifier, String.format("Found no valid %s instance!", UIID.class.getName()));
            return currentIdentifier;
        }
    }

    public String currentSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    public String getConversationId() {
        return currentSessionId() + currentUIID();
    }

    public Object get(String name, ObjectFactory<?> objectFactory) {
        return get(name, objectFactory, currentUIID());
    }

    public Object get(String name, ObjectFactory<?> objectFactory, UIID uuid) {
        logger.trace("Getting bean with name [{}] from UI space [{}]", name, uuid);
        final Map<String, Object> uiSpace = getObjectMap(uuid);
        Object bean = uiSpace.get(name);
        if (bean == null) {
            logger.trace("Bean [{}] not found in UI space [{}], invoking object factory", name, uuid);
            bean = objectFactory.getObject();
            if (bean instanceof UI) {
                ((UI) bean).addDetachListener(this);
            }
            if (!(bean instanceof Serializable)) {
                logger.warn("Storing non-serializable bean [{}] with name [{}] in UI space [{}]", bean, name, uuid);
            }
            uiSpace.put(name, bean);
        }
        logger.trace("Returning bean [{}] with name [{}] from UI space [{}]", bean, name, uuid);
        return bean;
    }

    public Object remove(String name) {
        return remove(name, currentUIID());
    }

    public Object remove(String name, UIID uuid) {
        logger.trace("Removing bean with name [{}] from UI space [{}]", name, uuid);
        try {
            getDestructionCallbackMap(uuid).remove(name);
            return getObjectMap(uuid).remove(name);
        } finally {
            cleanEmptyMaps(uuid);
        }
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        registerDestructionCallback(name, callback, currentUIID());
    }

    public void registerDestructionCallback(String name, Runnable callback, UIID uuid) {
        logger.trace("Registering destruction callback [{}] for bean with name [{}] in UI space [{}]", callback, name, uuid);
        if (!(callback instanceof Serializable)) {
            logger.warn("Storing non-serializable destruction callback [{}] for bean with name [{}] in UI space [{}]", callback, name, uuid);
        }
        getDestructionCallbackMap(uuid).put(name, callback);
    }

    private Map<String, Object> getObjectMap(UIID uuid) {
        Map<String, Object> map = objectMap.get(uuid);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            objectMap.put(uuid, map);
        }
        return map;
    }

    private void cleanEmptyMaps(UIID uuid) {
        Map<String, Object> uiSpace = objectMap.get(uuid);
        if (uiSpace != null && uiSpace.isEmpty()) {
            objectMap.remove(uuid);
        }
        Map<String, Runnable> destructionCallbacks = destructionCallbackMap.get(uuid);
        if (destructionCallbacks != null && destructionCallbacks.isEmpty()) {
            destructionCallbacks.remove(uuid);
        }
    }

    private Map<String, Runnable> getDestructionCallbackMap(UIID uuid) {
        Map<String, Runnable> map = destructionCallbackMap.get(uuid);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            destructionCallbackMap.put(uuid, map);
        }
        return map;
    }

    @Override
    public void detach(ClientConnector.DetachEvent event) {
        logger.debug("Received DetachEvent from [{}]", event.getSource());
        final UIID uiIdentifier = new UIID((UI) event.getSource());
        final Map<String, Runnable> destructionSpace = destructionCallbackMap.remove(uiIdentifier);
        if (destructionSpace != null) {
            for (Runnable runnable : destructionSpace.values()) {
                runnable.run();
            }
        }
        objectMap.remove(uiIdentifier);
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        logger.debug("UIStore bound to HTTP session [{}]", event.getSession().getId());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        logger.debug("UIStore unbound from HTTP session [{}]", event.getSession().getId());
    }
}
