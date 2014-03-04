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

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test case for {@link WeakListenerCollection}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class WeakListenerCollectionTest {

    @Test
    public void addListenerAndPublishEvent() {
        final Set<String> seenPayloads = new HashSet<>();
        final EventBusListener<String> stringListener = new EventBusListener<String>() {
            @Override
            public void onEvent(Event<String> event) {
                seenPayloads.add(event.getPayload());
            }
        };
        final WeakListenerCollection listenerList = new WeakListenerCollection();
        listenerList.add(stringListener);

        listenerList.publish(new Event<>(EventScope.APPLICATION, this, "hello"));

        assertTrue(seenPayloads.contains("hello"));
    }

    @Test
    public void addListenerOfOtherTypeAndPublishEvent() {
        final EventBusListener<Integer> integerListener = new EventBusListener<Integer>() {
            @Override
            public void onEvent(Event<Integer> event) {
                fail("Integer listener should not be called");
            }
        };

        final WeakListenerCollection listenerList = new WeakListenerCollection();
        listenerList.add(integerListener);

        listenerList.publish(new Event<>(EventScope.APPLICATION, this, "hello"));
    }

    @Test
    public void addListenerOfSupertypeAndPublishEvent() {
        final Set<Object> seenPayloads = new HashSet<>();
        final EventBusListener<Object> objectListener = new EventBusListener<Object>() {
            @Override
            public void onEvent(Event<Object> event) {
                seenPayloads.add(event.getPayload());
            }
        };
        final WeakListenerCollection listenerList = new WeakListenerCollection();
        listenerList.add(objectListener);

        listenerList.publish(new Event<>(EventScope.APPLICATION, this, "hello"));

        assertTrue(seenPayloads.contains("hello"));
    }
}
