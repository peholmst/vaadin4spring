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
package org.vaadin.spring.mvp;

import org.springframework.util.Assert;
import org.vaadin.spring.events.EventBus;

/**
 * Base class for a <em>Presenter</em> implementation.
 *
 * @param <T> <em>View</em> type
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
public abstract class Presenter<T> {

    private final T view;
    private final EventBus eventBus;

    /**
     * The constructor automatically subscribes to event bus events.
     *
     * @param view <em>View</em>
     * @param eventBus Vaadin even bus
     */
    public Presenter(T view, EventBus eventBus) {
        Assert.notNull(view);
        Assert.notNull(eventBus);
        this.view = view;
        this.eventBus = eventBus;
        eventBus.subscribe(this);
    }

    public T getView() {
        return view;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
