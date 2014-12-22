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
import org.springframework.beans.factory.ObjectFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for storing beans in the different Vaadin scopes. For internal use only.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class BeanStore implements Serializable {

	private static final long serialVersionUID = 7625347916717427098L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanStore.class);

    private final Map<String, Object> objectMap = new ConcurrentHashMap<String, Object>();

    private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<String, Runnable>();

    private final String identification;

    private final DestructionCallback destructionCallback;

    private boolean destroyed = false;

    public BeanStore(String identification, DestructionCallback destructionCallback) {
        LOGGER.debug("Initializing scope for [{}]", identification);
        this.identification = identification;
        this.destructionCallback = destructionCallback;
    }

    public BeanStore(String identification) {
        this(identification, null);
    }


    public Object get(String s, ObjectFactory<?> objectFactory) {
        LOGGER.trace("Getting bean with name [{}]", s);
        Object bean = objectMap.get(s);
        if (bean == null) {
            LOGGER.trace("Bean with name [{}] not found in store, creating new", s);
            bean = create(s, objectFactory);
            objectMap.put(s, bean);
        }
        return bean;
    }

    protected Object create(String s, ObjectFactory<?> objectFactory) {
        final Object bean = objectFactory.getObject();
        if (!(bean instanceof Serializable)) {
            LOGGER.warn("Storing non-serializable bean [{}] with name [{}]", bean, s);
        }
        return bean;
    }

    public Object remove(String s) {
        destructionCallbacks.remove(s);
        return objectMap.remove(s);
    }

    public void registerDestructionCallback(String s, Runnable runnable) {
        LOGGER.trace("Registering destruction callback for bean with name [{}]", s);
        destructionCallbacks.put(s, runnable);
    }

    public void destroy() {
        if (destroyed) {
            return;
        }
        try {
            LOGGER.debug("Destroying scope for [{}]", identification);
            for (Runnable destructionCallback : destructionCallbacks.values()) {
                destructionCallback.run();
            }
            destructionCallbacks.clear();
            objectMap.clear();
            if (destructionCallback != null) {
                destructionCallback.beanStoreDestoyed(this);
            }
        } finally {
            destroyed = true;
        }
    }

    /**
     * Callback interface for receiving notifications about a {@link org.vaadin.spring.internal.BeanStore} being destroyed.
     */
    public static interface DestructionCallback extends Serializable {

        void beanStoreDestoyed(BeanStore beanStore);

    }
}
