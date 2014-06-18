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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import static org.junit.Assert.*;

/**
 * Example test that uses the {@link org.vaadin.spring.test.VaadinAppConfiguration} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@VaadinAppConfiguration
@ContextConfiguration(classes = ExampleIntegrationTest.Config.class)
public class ExampleIntegrationTest {

    @Autowired
    MockHttpSession session;
    @Autowired
    MockHttpServletRequest request;
    @Autowired
    ExampleSessionData exampleSessionData;
    @Autowired
    ExampleUIScopedObject exampleUIScopedObject;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus applicationEvenBus;
    @Autowired
    @EventBusScope(EventScope.SESSION)
    EventBus sessionEventBus;
    @Autowired
    @EventBusScope(EventScope.UI)
    EventBus uiEventBus;

    @Test
    public void testAutowiring() {
        assertNotNull(request);
        assertNotNull(session);
        assertNotNull(exampleSessionData);
        assertNotNull(exampleUIScopedObject);
        assertNotNull(applicationEvenBus);
        assertNotNull(sessionEventBus);
        assertNotNull(uiEventBus);
    }

    @Test
    public void testSharedSessionData() {
        exampleSessionData.someData = "Hello";
        assertEquals("Hello", exampleUIScopedObject.exampleSessionData.someData);
    }

    @Test
    public void testUIEventBus() {
        uiEventBus.publish(this, "Hello");
        assertSame(this, exampleUIScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello", exampleUIScopedObject.lastReceivedEvent.getPayload());
    }

    @Test
    public void testSessionEventBus() {
        sessionEventBus.publish(this, "Hello2");
        assertSame(this, exampleUIScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello2", exampleUIScopedObject.lastReceivedEvent.getPayload());
    }

    @Test
    public void testApplicationEventBus() {
        applicationEvenBus.publish(this, "Hello3");
        assertSame(this, exampleUIScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello3", exampleUIScopedObject.lastReceivedEvent.getPayload());
    }

    @Configuration
    @EnableVaadin
    public static class Config {

        @Bean
        @UIScope
        ExampleUIScopedObject exampleUIScopedObject() {
            return new ExampleUIScopedObject();
        }

        @Bean
        @Scope("session")
        ExampleSessionData exampleSessionData() {
            return new ExampleSessionData();
        }

    }


}
