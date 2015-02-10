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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.annotation.EnableVaadin;
import org.vaadin.spring.annotation.VaadinSessionScope;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EnableVaadinEventBus;
import org.vaadin.spring.events.annotation.EventBusScope;
import org.vaadin.spring.navigator.annotation.VaadinViewScope;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Example test that uses the {@link org.vaadin.spring.test.annotation.VaadinAppConfiguration} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@VaadinAppConfiguration
@ContextConfiguration(classes = ExampleIntegrationTest.Config.class)
public class ExampleIntegrationTest {

    @Autowired
    ExampleSessionData exampleSessionData;
    @Autowired
    ExampleUIScopedObject exampleUIScopedObject;
    @Autowired
    ExampleViewScopedObject exampleViewScopedObject;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus applicationEvenBus;
    @Autowired
    @EventBusScope(EventScope.SESSION)
    EventBus sessionEventBus;
    @Autowired
    @EventBusScope(EventScope.UI)
    EventBus uiEventBus;
    @Autowired
    @EventBusScope(EventScope.VIEW)
    EventBus viewEventBus;

    @Test
    public void testAutowiring() {
        assertNotNull(exampleSessionData);
        assertNotNull(exampleUIScopedObject);
        assertNotNull(applicationEvenBus);
        assertNotNull(sessionEventBus);
        assertNotNull(uiEventBus);
        assertNotNull(viewEventBus);
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

    @Test
    public void testViewEventBus() {
        viewEventBus.publish(this, "Hello4");
        assertSame(this, exampleViewScopedObject.lastReceivedEvent.getSource());
        assertEquals("Hello4", exampleViewScopedObject.lastReceivedEvent.getPayload());
    }

    @Configuration
    @EnableVaadin
    @EnableVaadinEventBus
    public static class Config {

        @Bean
        @VaadinUIScope
        ExampleUIScopedObject exampleUIScopedObject() {
            return new ExampleUIScopedObject();
        }

        @Bean
        @VaadinViewScope
        ExampleViewScopedObject exampleViewScopedObject() {
            return new ExampleViewScopedObject();
        }

        @Bean
        @VaadinSessionScope
        ExampleSessionData exampleSessionData() {
            return new ExampleSessionData();
        }

    }


}
