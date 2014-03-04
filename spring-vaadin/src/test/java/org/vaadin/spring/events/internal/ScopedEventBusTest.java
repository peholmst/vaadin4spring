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
package org.vaadin.spring.events.internal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test case for {@link org.vaadin.spring.events.internal.ScopedEventBus}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ScopedEventBusTest {

    ScopedEventBus applicationEventBus;
    ScopedEventBus sessionEventBus;

    interface StringListener extends EventBusListener<String> {
    }

    @Before
    public void setUp() {
        applicationEventBus = new ScopedEventBus(EventScope.APPLICATION);
        sessionEventBus = new ScopedEventBus(EventScope.SESSION, applicationEventBus);
    }

    @After
    public void tearDown() {
        sessionEventBus.destroy();
        applicationEventBus.destroy();
    }

    @Test
    public void testSubscribeAndPublish() {
        StringListener stringListener = mock(StringListener.class);

        sessionEventBus.subscribe(stringListener);
        sessionEventBus.publish(this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener).onEvent(event.capture());
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPublishToInvalidScope() {
        applicationEventBus.publish(EventScope.SESSION, this, "fail");
    }

    @Test
    public void testPublishToParentScope() {
        StringListener stringListener = mock(StringListener.class);

        applicationEventBus.subscribe(stringListener);
        sessionEventBus.publish(EventScope.APPLICATION, this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener).onEvent(event.capture());
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test
    public void testPropagateToChild() {
        StringListener stringListener = mock(StringListener.class);

        sessionEventBus.subscribe(stringListener);
        applicationEventBus.publish(this, "Hello World");

        ArgumentCaptor<Event> event = ArgumentCaptor.forClass(Event.class);
        verify(stringListener).onEvent(event.capture());
        assertEquals("Hello World", event.getValue().getPayload());
    }

    @Test
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
}
