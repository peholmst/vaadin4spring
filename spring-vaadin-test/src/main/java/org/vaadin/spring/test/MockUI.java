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
package org.vaadin.spring.test;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.vaadin.spring.internal.UIID;
import org.vaadin.spring.internal.UIScope;

/**
 * Mock implementation of {@link com.vaadin.ui.UI} that is always present in the application context when testing
 * {@link org.vaadin.spring.UIScope}d classes, provided that {@link org.vaadin.spring.test.VaadinAppConfiguration} is used.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public final class MockUI extends UI {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockUI.class);
    private static int nextUIId = 0;
    private final int uiId;
    private final UIID uiIdentifier;

    MockUI(int uiId) {
        LOGGER.debug("MockUI with uiId {} created", uiId);
        this.uiId = uiId;
        this.uiIdentifier = new UIID(uiId);
    }

    /**
     * Sets up the Mock UI for the specified application context and current thread.
     *
     * @param applicationContext the application context. If the context is an instance of {@link org.springframework.context.ConfigurableApplicationContext},
     *                           the Mock UI instance will be registered with the real UI scope, which means that UI detachment
     *                           and {@code PreDestroy} methods will work as expected.
     */
    public static synchronized void setUp(ApplicationContext applicationContext) {
        LOGGER.debug("Setting up MockUI for application context [{}]", applicationContext);
        final MockUI ui = new MockUI(++nextUIId);
        UI.setCurrent(ui);
        CurrentInstance.set(UIID.class, ui.getUiIdentifier());
        if (applicationContext instanceof ConfigurableApplicationContext) {
            final ConfigurableApplicationContext cac = (ConfigurableApplicationContext) applicationContext;
            final ConfigurableListableBeanFactory bf = cac.getBeanFactory();
            // Register the UI with the UI Scope (to get detach events, etc.)
            LOGGER.debug("Registering [{}] with the UI Scope", ui);
            bf.getRegisteredScope(UIScope.UI_SCOPE_NAME).get("mockUI", new ObjectFactory<MockUI>() {
                @Override
                public MockUI getObject() throws BeansException {
                    return ui;
                }
            });
        } else {
            LOGGER.warn("Application context was not configurable - the MockUI has not been registered with the UI Scope");
        }
    }

    /**
     * Detaches the Mock UI and clears it from the current thread. If the Mock UI was properly registered with the UI Scope
     * in {@link #setUp(org.springframework.context.ApplicationContext)}, the UI scope will also be destroyed.
     */
    public static void tearDown() {
        if (UI.getCurrent() != null) {
            LOGGER.debug("Detaching MockUI [{}] after test", UI.getCurrent());
            UI.getCurrent().detach();
        }
        UI.setCurrent(null);
        CurrentInstance.set(UIID.class, null);
    }

    UIID getUiIdentifier() {
        return uiIdentifier;
    }

    @Override
    public int getUIId() {
        return uiId;
    }

    @Override
    public void detach() {
        LOGGER.debug("Firing DetachEvent");
        fireEvent(new DetachEvent(this));
    }

    @Override
    protected void init(VaadinRequest request) {
    }
}
