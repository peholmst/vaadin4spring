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
package org.vaadin.spring.test;

import com.vaadin.navigator.View;
import org.vaadin.spring.internal.BeanStore;
import org.vaadin.spring.navigator.internal.ViewCache;

/**
 * Simple implementation of {@link org.vaadin.spring.navigator.internal.ViewCache} that always returns
 * the same {@link org.vaadin.spring.internal.BeanStore} instance. This effectively means that exactly one
 * view scope is always active. The view provider callbacks {@link #creatingView(String)} and {@link #viewCreated(String, com.vaadin.navigator.View)} do not do anything.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SimpleViewCache implements ViewCache {

    private final BeanStore beanStore;

    public SimpleViewCache(String beanStoreName) {
        beanStore = new BeanStore(beanStoreName);
    }

    @Override
    public void creatingView(String viewName) {
        // NOP
    }

    @Override
    public void viewCreated(String viewName, View viewInstance) {
        // NOP
    }

    @Override
    public BeanStore getCurrentViewBeanStore() throws IllegalStateException {
        return beanStore;
    }
}
