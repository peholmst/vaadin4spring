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
package org.vaadin.spring.events;

import org.springframework.beans.factory.Aware;

/**
 * Marker super interface for beans that want to get notified of a specific type of event bus.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface EventBusAware extends Aware {

    /**
     * Interface to be implemented by beans that want to get notified of the
     * application event bus.
     */
    interface ApplicationEventBusAware extends EventBusAware {
        /**
         * Sets the application scoped event bus.
         */
        void setApplicationEventBus(EventBus.ApplicationEventBus applicationEventBus);
    }

    /**
     * Interface to be implemented by beans that want to get notified of the
     * session event bus.
     */
    interface SessionEventBusAware extends EventBusAware {
        /**
         * Sets the session scoped event bus.
         */
        void setSessionEventBus(EventBus.SessionEventBus sessionEventBus);
    }

    /**
     * Interface to be implemented by beans that want to get notified of the
     * UI event bus.
     */
    interface UIEventBusAware extends EventBusAware {
        /**
         * Sets the UI scoped event bus.
         */
        void setUIEventBus(EventBus.UIEventBus uiEventBus);
    }

    /**
     * Interface to be implemented by beans that want to get notified of the
     * view event bus.
     */
    interface ViewEventBusAware extends EventBusAware {
        /**
         * Sets the view scoped event bus.
         */
        void setViewEventBus(EventBus.ViewEventBus viewEventBus);
    }
}
