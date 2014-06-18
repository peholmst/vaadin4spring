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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Example class used by {@link org.vaadin.spring.test.ExampleIntegrationTest}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ExampleUIScopedObject implements EventBusListener<String> {

    private final static Logger logger = LoggerFactory.getLogger(ExampleUIScopedObject.class);

    @Autowired
    EventBus uiEventBus;

    @Autowired
    ExampleSessionData exampleSessionData;
    Event<String> lastReceivedEvent;

    @PostConstruct
    void init() {
        logger.info("Init {}", this);
        uiEventBus.subscribe(this);
    }

    @PreDestroy
    void destroy() {
        logger.info("Destroy {}", this);
        uiEventBus.unsubscribe(this);
    }

    @Override
    public void onEvent(Event<String> event) {
        this.lastReceivedEvent = event;
    }
}
