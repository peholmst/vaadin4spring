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
package org.vaadin.spring.events;

import org.vaadin.spring.UIScope;

/**
 * An event bus for {@link org.vaadin.spring.events.EventScope#UI} scoped events. The event bus
 * can be used as is, or chained to a {@link org.vaadin.spring.events.SessionEventBus}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@UIScope
public class UIEventBus extends ScopedEventBus {

    public UIEventBus() {
    }

    public UIEventBus(SessionEventBus parentEventBus) {
        super(parentEventBus);
    }

    @Override
    protected EventScope getScope() {
        return EventScope.UI;
    }
}
