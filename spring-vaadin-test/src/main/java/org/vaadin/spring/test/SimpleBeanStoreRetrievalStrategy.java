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

import org.vaadin.spring.internal.BeanStore;
import org.vaadin.spring.internal.BeanStoreRetrievalStrategy;

/**
 * Simple implementation of {@link org.vaadin.spring.internal.BeanStoreRetrievalStrategy} that always returns
 * the same {@link org.vaadin.spring.internal.BeanStore} instance.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
class SimpleBeanStoreRetrievalStrategy implements BeanStoreRetrievalStrategy {

    private final BeanStore beanStore;
    private final String conversationId;

    public SimpleBeanStoreRetrievalStrategy(String conversationId) {
        this.conversationId = conversationId;
        this.beanStore = new BeanStore(conversationId);
    }

    @Override
    public BeanStore getBeanStore() {
        return beanStore;
    }

    @Override
    public String getConversationId() {
        return conversationId;
    }
}
