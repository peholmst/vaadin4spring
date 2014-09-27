package org.vaadin.spring.test;

import org.vaadin.spring.internal.BeanStore;
import org.vaadin.spring.internal.BeanStoreRetrievalStrategy;

/**
 * Simple implementation of {@link org.vaadin.spring.internal.BeanStoreRetrievalStrategy} that always returns
 * the same {@link org.vaadin.spring.internal.BeanStore} instance.
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
