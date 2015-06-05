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
package org.vaadin.spring.events.internal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test case for {@link org.vaadin.spring.events.internal.ScopedEventBus}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ScopedEventBusTest {

    ScopedEventBus.DefaultApplicationEventBus applicationEventBus;
    ScopedEventBus.DefaultSessionEventBus sessionEventBus;

    interface StringListener extends EventBusListener<String> {
    }

    static class MultipleListeners {

        Event<String> theStringEvent;
        Event<Integer> theIntegerEvent;
        String theStringPayload;
        Integer theIntegerPayload;

        @EventBusListenerMethod
        void onStringEvent(Event<String> stringEvent) {
            theStringEvent = stringEvent;
        }

        @EventBusListenerMethod
        void onStringPayloadEvent(String stringPayload) {
            theStringPayload = stringPayload;
        }

        @EventBusListenerMethod
        void onIntegerEvent(Event<Integer> integerEvent) {
            theIntegerEvent = integerEvent;
        }

        @EventBusListenerMethod
        void onIntegerPayloadEvent(Integer integerPayload) {
            theIntegerPayload = integerPayload;
        }
    }

    static class InvalidListener1 {

        @EventBusListenerMethod
        void tooFewParameters() {
        }
    }

    static class InvalidListener2 {

        @EventBusListenerMethod
        void tooManyParameters(String parameter1, Integer parameter2) {
        }
    }

    @Before
    public void setUp() {
        applicationEventBus = new ScopedEventBus.DefaultApplicationEventBus();
        sessionEventBus = new ScopedEventBus.DefaultSessionEventBus(applicationEventBus);
    }

    @After
    public void tearDown() {
        sessionEventBus.destroy();
        applicationEventBus.destroy();
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testSubscribeAndPublish() {
        StringListener stringListener = mock(StringListener.class);

        sessionEventBus.subscribe(stringListener);
        sessionEventBus.publish(this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener).onEvent(event.capture());
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test
    public void testSubscribeAndPublishWithListenerMethods() {
        MultipleListeners listener = new MultipleListeners();

        sessionEventBus.subscribe(listener);
        sessionEventBus.publish(this, "Hello World");

        assertNull(listener.theIntegerEvent);
        assertNull(listener.theIntegerPayload);
        assertNotNull(listener.theStringEvent);
        assertEquals("Hello World", listener.theStringEvent.getPayload());
        assertEquals("Hello World", listener.theStringPayload);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubscribeAndPublishWithListenerMethodsAndTooFewParameters() {
        sessionEventBus.subscribe(new InvalidListener1());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubscribeAndPublishWithListenerMethodsAndTooManyParameters() {
        sessionEventBus.subscribe(new InvalidListener2());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPublishToInvalidScope() {
        applicationEventBus.publish(EventScope.SESSION, this, "fail");
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testPublishToParentScope() {
        StringListener stringListener = mock(StringListener.class);

        applicationEventBus.subscribe(stringListener);
        sessionEventBus.publish(EventScope.APPLICATION, this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener).onEvent(event.capture());
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test
    public void testPublishToParentScopeWithListenerMethods() {
        MultipleListeners listener = new MultipleListeners();

        applicationEventBus.subscribe(listener);
        sessionEventBus.publish(EventScope.APPLICATION, this, "Hello World");

        assertNull(listener.theIntegerEvent);
        assertNull(listener.theIntegerPayload);
        assertNotNull(listener.theStringEvent);
        assertEquals("Hello World", listener.theStringEvent.getPayload());
        assertEquals("Hello World", listener.theStringPayload);
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testPropagateToChild() {
        StringListener stringListener = mock(StringListener.class);

        sessionEventBus.subscribe(stringListener);
        applicationEventBus.publish(this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener).onEvent(event.capture());
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test
    public void testPropagateToChildWithListenerMethods() {
        MultipleListeners listener = new MultipleListeners();

        applicationEventBus.subscribe(listener);
        applicationEventBus.publish(this, "Hello World");

        assertNull(listener.theIntegerEvent);
        assertNull(listener.theIntegerPayload);
        assertNotNull(listener.theStringEvent);
        assertEquals("Hello World", listener.theStringEvent.getPayload());
        assertEquals("Hello World", listener.theStringPayload);
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testNoPropagationToChild() {
        StringListener stringListener = mock(StringListener.class);

        sessionEventBus.subscribe(stringListener, false);
        applicationEventBus.publish(this, "Hello World");
        sessionEventBus.publish(this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener, only()).onEvent(event.capture());
        verifyNoMoreInteractions(stringListener);
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test
    public void testNoPropagationToChildWithListenerMethods() {
        MultipleListeners listener = new MultipleListeners();

        sessionEventBus.subscribe(listener, false);
        sessionEventBus.publish(this, "Hello World Session");
        applicationEventBus.publish(this, "Hello World Application");

        assertNull(listener.theIntegerEvent);
        assertNull(listener.theIntegerPayload);
        assertNotNull(listener.theStringEvent);
        assertEquals("Hello World Session", listener.theStringEvent.getPayload());
        assertEquals("Hello World Session", listener.theStringPayload);
    }

}
