/*
 * Copyright 2014, 2015 The original authors
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Example class used by {@link ExampleIntegrationTest}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ExampleViewScopedObject implements EventBusListener<String> {

    private static final long serialVersionUID = 8217357935429748277L;

    private final static Logger logger = LoggerFactory.getLogger(ExampleViewScopedObject.class);

    @Autowired
    @EventBusScope(EventScope.VIEW)
    EventBus viewEventBus;

    Event<String> lastReceivedEvent;

    @PostConstruct
    void init() {
        logger.info("Init {}", this);
        viewEventBus.subscribe(this);
    }

    @PreDestroy
    void destroy() {
        logger.info("Destroy {}", this);
        viewEventBus.unsubscribe(this);
    }

    @Override
    public void onEvent(Event<String> event) {
        this.lastReceivedEvent = event;
    }
}
