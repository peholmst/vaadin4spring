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
package org.vaadin.spring.context;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NamedBean;

import com.vaadin.flow.server.VaadinSession;

/**
 * Factory bean that makes the current {@link com.vaadin.server.VaadinSession} available
 * for injection. Only works if {@link com.vaadin.server.VaadinSession#getCurrent()} does not
 * return {@code null}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinSessionFactory implements FactoryBean<VaadinSession>, NamedBean {

    public static final String BEAN_NAME = "vaadinSessionFactory";

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }

    @Override
    public VaadinSession getObject() throws Exception {
        final VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            throw new IllegalStateException("No VaadinSession bound to current thread");
        }
        return session;
    }

    @Override
    public Class<?> getObjectType() {
        return VaadinSession.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
