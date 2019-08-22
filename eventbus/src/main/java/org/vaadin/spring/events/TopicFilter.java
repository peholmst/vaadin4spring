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
package org.vaadin.spring.events;

/**
 * The interface defines a method to validate a given event topic
 * an listener topic. The event topic is provided when publishing
 * an event and the listener topic by the {@link org.vaadin.spring.events.annotation.EventBusListenerTopic} 
 * annotation. An implementation of this interface can be used as 
 * parameter of this annotation.
 * 
 * @author Marco Luthardt (marco.luthardt@iandme.net)
 * @see org.vaadin.spring.events.annotation.EventBusListenerTopic
 */
public interface TopicFilter {

    /**
     * Validates the given event topic against the listener topic.
     * 
     * @param eventTopic    the topic provided by while publishing an event, never {@code null}
     * @param listenerTopic the topic of the listener method, never {@code null}
     * 
     * @return true true if the event topic matches the listener topic, otherwise false
     */
    boolean validTopic(String eventTopic, String listenerTopic);

}
