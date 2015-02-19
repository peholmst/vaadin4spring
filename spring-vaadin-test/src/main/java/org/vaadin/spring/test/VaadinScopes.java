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

import org.vaadin.spring.internal.VaadinSessionScope;
import org.vaadin.spring.internal.VaadinUIScope;
import org.vaadin.spring.navigator.internal.VaadinViewScope;

/**
 * Helper class to set up the Vaadin scopes for testing.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public final class VaadinScopes {

    private VaadinScopes() {
    }

    /**
     * Sets up {@link org.vaadin.spring.annotation.VaadinSessionScope} and {@link org.vaadin.spring.annotation.VaadinUIScope} to work as expected
     * during the test. Please note, that both {@link com.vaadin.ui.UI#getCurrent()} and {@link com.vaadin.server.VaadinSession#getCurrent()} will
     * return null.
     */
    public static void setUp() {
        VaadinSessionScope.setBeanStoreRetrievalStrategy(new SimpleBeanStoreRetrievalStrategy("mock session scope"));
        VaadinUIScope.setBeanStoreRetrievalStrategy(new SimpleBeanStoreRetrievalStrategy("mock UI scope"));
        VaadinViewScope.setViewCacheRetrievalStrategy(new SimpleViewCacheRetrievalStrategy("mock view scope"));
    }

    /**
     * Tears down {@link org.vaadin.spring.annotation.VaadinSessionScope} and {@link org.vaadin.spring.annotation.VaadinUIScope}, causing all scoped beans to be destroyed.
     */
    public static void tearDown() {
        VaadinViewScope.getViewCacheRetrievalStrategy().getViewCache(null).getCurrentViewBeanStore().destroy();
        VaadinUIScope.getBeanStoreRetrievalStrategy().getBeanStore().destroy();
        VaadinSessionScope.getBeanStoreRetrievalStrategy().getBeanStore().destroy();
        VaadinViewScope.setViewCacheRetrievalStrategy(null);
        VaadinUIScope.setBeanStoreRetrievalStrategy(null);
        VaadinSessionScope.setBeanStoreRetrievalStrategy(null);
    }
}
