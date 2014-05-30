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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.internal.UIID;

/**
 * Mock implementation of {@link com.vaadin.ui.UI} that is always present in the application context when testing
 * {@link org.vaadin.spring.UIScope}d classes, provided that {@link org.vaadin.spring.test.VaadinAppConfiguration} is used.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public final class MockUI extends UI {

    private final static Logger logger = LoggerFactory.getLogger(MockUI.class);
    private final int uiId;
    private final UIID uiIdentifier;

    MockUI(int uiId) {
        this.uiId = uiId;
        this.uiIdentifier = new UIID(uiId);
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
        logger.debug("Firing DetachEvent");
        fireEvent(new DetachEvent(this));
    }

    @Override
    protected void init(VaadinRequest request) {
    }
}
