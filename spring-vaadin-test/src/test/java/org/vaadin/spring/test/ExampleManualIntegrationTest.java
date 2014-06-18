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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import javax.inject.Provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Example test that manually sets up and tears down the Mock UI, for situations where you for some
 * reason cannot use the {@link org.vaadin.spring.test.VaadinAppConfiguration} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = ExampleManualIntegrationTest.Config.class)
public class ExampleManualIntegrationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Provider<ExampleSessionData> exampleSessionData;
    @Autowired
    Provider<ExampleUIScopedObject> exampleUIScopedObject;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus applicationEvenBus;
    @Autowired
    @EventBusScope(value = EventScope.SESSION, proxy = true)
    EventBus sessionEventBus;
    @Autowired
    @EventBusScope(value = EventScope.UI, proxy = true)
    EventBus uiEventBus;

    @Before
    public void setUp() {
        MockUI.setUp(applicationContext);
    }

    @After
    public void tearDown() {
        MockUI.tearDown();
    }

    @Test
    public void testAutowiring() {
        assertNotNull(exampleSessionData);
        assertNotNull(exampleUIScopedObject);
        assertNotNull(applicationEvenBus);
        assertNotNull(sessionEventBus);
        assertNotNull(uiEventBus);
    }

    @Test
    public void testSharedSessionData() {
        exampleSessionData.get().someData = "Hello";
        assertEquals("Hello", exampleUIScopedObject.get().exampleSessionData.someData);
    }

    @Test
    public void testUIEventBus() {
        final ExampleUIScopedObject uiScopedObject = exampleUIScopedObject.get();
        uiEventBus.publish(this, "Hello");
        assertSame(this, uiScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello", uiScopedObject.lastReceivedEvent.getPayload());
    }

    @Test
    public void testSessionEventBus() {
        final ExampleUIScopedObject uiScopedObject = exampleUIScopedObject.get();
        assertNotNull(uiScopedObject);
        sessionEventBus.publish(this, "Hello2");
        assertSame(this, uiScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello2", uiScopedObject.lastReceivedEvent.getPayload());
    }

    @Test
    public void testApplicationEventBus() {
        final ExampleUIScopedObject uiScopedObject = exampleUIScopedObject.get();
        applicationEvenBus.publish(this, "Hello3");
        assertSame(this, uiScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello3", uiScopedObject.lastReceivedEvent.getPayload());
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
