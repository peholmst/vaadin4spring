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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * An event bus for {@link org.vaadin.spring.events.EventScope#APPLICATION} scoped events. Any events published using
 * the {@link org.springframework.context.ApplicationContext#publishEvent(org.springframework.context.ApplicationEvent)} method
 * will also be published on this bus. However, any events published on this bus directly will NOT be propagated to the Spring application context.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ApplicationEventBus extends ScopedEventBus implements ApplicationListener<ApplicationEvent> {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    protected EventScope getScope() {
        return EventScope.APPLICATION;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        logger.debug(String.format("Propagating application event [%s] to event bus [%s]", event, this));
        publish(event.getSource(), event);
    }
}
