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

import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Uniquely identifies a UI instance for a given window/tab inside a session.
 * This is basically a wrapper for {@link com.vaadin.ui.UI#getUIId()}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 */
public class VaadinUIIdentifier implements Serializable {
    private final int uiId;

    public VaadinUIIdentifier(UICreateEvent createEvent) {
        this.uiId = createEvent.getUiId();
    }

    public VaadinUIIdentifier(UI ui) {
        Assert.notNull(ui, "ui must not be null");
        this.uiId = ui.getUIId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VaadinUIIdentifier that = (VaadinUIIdentifier) o;

        if (uiId != that.uiId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uiId;
    }

    @Override
    public String toString() {
        return String.format("UI:%d", uiId);
    }
}
