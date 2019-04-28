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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.HierachyTopicFilter;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.events.config.EventBusConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test case for {@link org.vaadin.spring.events.internal.ScopedEventBus}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { EventBusConfiguration.class })
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

        Event<String> theStringEventWithTopic;
        Event<Integer> theIntegerEventWithTopic;
        String theStringPayloadWithTopic;
        Integer theIntegerPayloadWithTopic;

        String theStringPayloadWithTopicFail;
        Integer theIntegerPayloadWithTopicFail;

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

        @EventBusListenerTopic(topic = "shouldSucceed")
        @EventBusListenerMethod
        void onStringEventWithTopic(Event<String> stringEvent) {
            theStringEventWithTopic = stringEvent;
        }

        @EventBusListenerTopic(topic = "shouldSucceed")
        @EventBusListenerMethod
        void onStringPayloadEventWithTopic(String stringPayload) {
            theStringPayloadWithTopic = stringPayload;
        }

        @EventBusListenerTopic(topic = "shouldSucceed")
        @EventBusListenerMethod
        void onIntegerEventWithTopic(Event<Integer> integerEvent) {
            theIntegerEventWithTopic = integerEvent;
        }

        @EventBusListenerTopic(topic = "shouldSucceed", filter = HierachyTopicFilter.class)
        @EventBusListenerMethod
        void onIntegerPayloadEventWithTopic(Integer integerPayload) {
            theIntegerPayloadWithTopic = integerPayload;
        }

        @EventBusListenerTopic(topic = "shouldFail")
        @EventBusListenerMethod
        void onStringPayloadEventWithTopicFail(String stringPayload) {
            theStringPayloadWithTopicFail = stringPayload;
        }

        @EventBusListenerTopic(topic = "shouldSucceed.butFail")
        @EventBusListenerMethod
        void onIntegerPayloadEventWithTopicFail(Integer integerPayload) {
            theIntegerPayloadWithTopicFail = integerPayload;
        }
    }

    class TopicStringListener implements EventBusListener<String> {

        Event<String> theStringEvent;

        @Override
        public void onEvent(Event<String> event) {
            this.theStringEvent = event;
        }
    }

    class TopicIntegerListener implements EventBusListener<Integer> {

        Event<Integer> theIntegerEvent;

        @Override
        public void onEvent(Event<Integer> event) {
            this.theIntegerEvent = event;
        }
    }

    class TopicListeners {

        String theStringPayload;
        Integer theIntegerPayload;
        Event<String> theStringEvent;
        Event<Integer> theIntegerEvent;

        @EventBusListenerMethod
        void onStringEvent(String theStringPayload) {
            this.theStringPayload = theStringPayload;
        }

        @EventBusListenerMethod
        void onStringEvent(Event<String> theStringEvent) {
            this.theStringEvent = theStringEvent;
        }

        @EventBusListenerMethod
        void onIntegerEvent(Integer theIntegerPayload) {
            this.theIntegerPayload = theIntegerPayload;
        }

        @EventBusListenerMethod
        void onIntegerEvent(Event<Integer> theIntegerEvent) {
            this.theIntegerEvent = theIntegerEvent;
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

    @BeforeEach
    public void setUp() {
        applicationEventBus = new ScopedEventBus.DefaultApplicationEventBus();
        sessionEventBus = new ScopedEventBus.DefaultSessionEventBus(applicationEventBus);
    }

    @AfterEach
    public void tearDown() {
        sessionEventBus.destroy();
        applicationEventBus.destroy();
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
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

    @Test
    public void testSubscribeAndPublishWithListenerMethodsWithTopic() {
        MultipleListeners listener = new MultipleListeners();

        sessionEventBus.subscribe(listener);
        sessionEventBus.publish("shouldSucceed", this, "Hello World");
        sessionEventBus.publish("shouldSucceed.int", this, 10);

        // null because not called with topic
        assertNull(listener.theStringPayload);
        assertNull(listener.theStringEvent);
        assertNull(listener.theIntegerPayload);
        assertNull(listener.theIntegerEvent);

        // null because topic must fail
        assertNull(listener.theStringPayloadWithTopicFail);
        assertNull(listener.theIntegerPayloadWithTopicFail);
        assertNull(listener.theIntegerEventWithTopic);

        assertNotNull(listener.theStringPayloadWithTopic);
        assertNotNull(listener.theStringEventWithTopic);

        assertEquals("Hello World", listener.theStringPayloadWithTopic);
        assertEquals("Hello World", listener.theStringEventWithTopic.getPayload());
        assertEquals(10, listener.theIntegerPayloadWithTopic.intValue());
    }

    @Test
    public void testSubscribeToTopicAndPublishWithTopic() {
        String topic = "/news";
        String payload = "Hello World";

        TopicListeners topicListener = new TopicListeners();
        applicationEventBus.subscribe(topicListener, topic);

        applicationEventBus.publish(topic, this, payload);
        assertEquals(payload, topicListener.theStringPayload);
        assertEquals(payload, topicListener.theStringEvent.getPayload());
        assertNull(topicListener.theIntegerPayload);
        assertNull(topicListener.theIntegerEvent);
    }

    @Test
    public void testUnsubscribeFromTopicAndPublishWithTopic() {
        String topic = "/news";
        String payload = "Hello World";

        TopicListeners topicListener = new TopicListeners();
        applicationEventBus.subscribe(topicListener, topic);

        applicationEventBus.publish(topic, this, payload);
        assertEquals(payload, topicListener.theStringPayload);

        topicListener.theStringPayload = null;
        applicationEventBus.unsubscribe(topicListener);

        applicationEventBus.publish(topic, this, payload);
        assertNotEquals(payload, topicListener.theStringPayload);
    }

    @Test
    public void testSubscribeToTopicAndPublishWithDifferentTopic() {
        String topic = "/news";
        String differentTopic = "/different";
        String payload = "Hello World";

        TopicListeners topicListener = new TopicListeners();
        applicationEventBus.subscribe(topicListener, topic);

        applicationEventBus.publish(differentTopic, this, payload);
        assertNotEquals(payload, topicListener.theStringPayload);
    }

    @Test
    public void testSubscribeTopicListenerForTwoTopicsAndPublishWithThoseTopics() {
        String newsTopic = "/news";
        String counterTopic = "/counter";
        String newsPayload = "Hello World";
        Integer counterPayload = 0;

        TopicListeners topicListener = new TopicListeners();
        applicationEventBus.subscribe(topicListener, newsTopic);
        applicationEventBus.subscribe(topicListener, counterTopic);

        applicationEventBus.publish(newsTopic, this, newsPayload);
        assertEquals(newsPayload, topicListener.theStringPayload);
        assertNotEquals(counterPayload, topicListener.theIntegerPayload);

        applicationEventBus.publish(counterTopic, this, counterPayload);
        assertEquals(counterPayload, topicListener.theIntegerPayload);
    }

    @Test
    public void testTwoTopicListenersSubscribedForSameTopic() {
        String topic = "/news";
        String payload = "Hello World";

        TopicListeners firstTopicListener = new TopicListeners();
        TopicListeners secondTopicListener = new TopicListeners();

        applicationEventBus.subscribe(firstTopicListener, topic);
        applicationEventBus.subscribe(secondTopicListener, topic);

        applicationEventBus.publish(topic, this, payload);
        assertEquals(payload, firstTopicListener.theStringPayload);
        assertEquals(payload, firstTopicListener.theStringEvent.getPayload());
        assertEquals(payload, secondTopicListener.theStringPayload);
        assertEquals(payload, secondTopicListener.theStringEvent.getPayload());
    }

    @Test
    public void testTwoTopicListenersSubscribedForDifferentTopics() {
        String firstTopic = "/first";
        String secondTopic = "/second";
        String firstPayload = "first";
        String secondPayload = "second";

        TopicListeners firstTopicListener = new TopicListeners();
        TopicListeners secondTopicListener = new TopicListeners();

        applicationEventBus.subscribe(firstTopicListener, firstTopic);
        applicationEventBus.subscribe(secondTopicListener, secondTopic);

        applicationEventBus.publish(firstTopic, this, firstPayload);
        assertEquals(firstPayload, firstTopicListener.theStringPayload);
        assertEquals(firstPayload, firstTopicListener.theStringEvent.getPayload());
        assertNull(secondTopicListener.theStringPayload);
        assertNull(secondTopicListener.theStringEvent);

        applicationEventBus.publish(secondTopic, this, secondPayload);
        assertEquals(secondPayload, secondTopicListener.theStringPayload);
        assertEquals(secondPayload, secondTopicListener.theStringEvent.getPayload());
        assertNotEquals(secondPayload, firstTopicListener.theStringPayload);
        assertNotEquals(secondPayload, firstTopicListener.theStringEvent.getPayload());
    }

    @Test
    public void testSubscribeAndPublishWithTopic() {
        String topic = "/news";
        String payload = "Hello World";
        TopicStringListener stringListener = new TopicStringListener();

        sessionEventBus.subscribe(stringListener, topic);
        sessionEventBus.publish(topic, this, payload);
        assertEquals(payload, stringListener.theStringEvent.getPayload());
    }

    @Test
    public void testSubscribeAndPublishWithDifferentTopic() {
        String topic = "/news";
        String payload = "Hello World";
        String differentTopic = payload + ".extension";
        TopicStringListener stringListener = new TopicStringListener();

        sessionEventBus.subscribe(stringListener, topic);
        sessionEventBus.publish(differentTopic, this, payload);
        assertNull(stringListener.theStringEvent);
    }

    @Test
    public void testSubscribeAndPublishWithSameTopicButDifferentPayloadType() {
        String topic = "/news";
        Integer payload = 0;
        TopicStringListener stringListener = new TopicStringListener();

        sessionEventBus.subscribe(stringListener, topic);
        sessionEventBus.publish(topic, this, payload);
        assertNull(stringListener.theStringEvent);
    }

    @Test
    public void testSubscribeTwoTopicListenersForSameTopicWithDifferentPayloadType() {
        String topic = "/news";
        String stringPayload = "Hello World";
        Integer integerPayload = 0;
        TopicStringListener stringListener = new TopicStringListener();
        TopicIntegerListener integerListener = new TopicIntegerListener();

        sessionEventBus.subscribe(stringListener, topic);
        sessionEventBus.subscribe(integerListener, topic);

        sessionEventBus.publish(topic, this, integerPayload);
        assertNotNull(integerListener.theIntegerEvent);
        assertNull(stringListener.theStringEvent);

        sessionEventBus.publish(topic, this, stringPayload);
        assertNotNull(integerListener.theIntegerEvent);
        assertNotNull(stringListener.theStringEvent);
    }

    @Test
    public void testSubscribeAndPublishWithListenerMethodsAndTooFewParameters() {
        assertThrows(IllegalArgumentException.class, () -> sessionEventBus.subscribe(new InvalidListener1()));
    }

    @Test
    public void testSubscribeAndPublishWithListenerMethodsAndTooManyParameters() {
        assertThrows(IllegalArgumentException.class, () -> sessionEventBus.subscribe(new InvalidListener2()));
    }

    @Test
    public void testPublishToInvalidScope() {
        assertThrows(UnsupportedOperationException.class, () -> applicationEventBus.publish(EventScope.SESSION, this, "fail"));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
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
    @SuppressWarnings({"unchecked", "rawtypes"})
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
    @SuppressWarnings({"unchecked", "rawtypes"})
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

    @Test
    public void testSubscribeWithWeakReference() {
        StringListener listener = new StringListener() {
            int cnt = 0;

            @Override
            public void onEvent(Event<String> event) {
                cnt++;
                if (cnt > 1) {
                    fail("I should have been garbage collected by now");
                }
            }
        };
        applicationEventBus.subscribeWithWeakReference(listener);
        applicationEventBus.publish(this, "Hello World Application");
        listener = null;
        System.gc();
        applicationEventBus.publish(this, "Hello World Application");
    }
}
