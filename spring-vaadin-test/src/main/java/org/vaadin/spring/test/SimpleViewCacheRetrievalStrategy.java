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

import org.springframework.beans.factory.BeanFactory;
import org.vaadin.spring.navigator.internal.ViewCache;
import org.vaadin.spring.navigator.internal.ViewCacheRetrievalStrategy;

/**
 * Simple implementation of {@link org.vaadin.spring.navigator.internal.ViewCacheRetrievalStrategy} that always
 * returns the same instance of {@link org.vaadin.spring.test.SimpleViewCache}. This effectively means that exactly
 * one view scope is always active.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SimpleViewCacheRetrievalStrategy implements ViewCacheRetrievalStrategy {

    private final ViewCache viewCache;

    public SimpleViewCacheRetrievalStrategy(String beanStoreName) {
        viewCache = new SimpleViewCache(beanStoreName);
    }

    @Override
    public ViewCache getViewCache(BeanFactory beanFactory) {
        return viewCache;
    }
}
