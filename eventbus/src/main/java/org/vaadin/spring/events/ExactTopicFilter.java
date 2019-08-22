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
 * An implementation of {@link org.vaadin.spring.events.TopicFilter}
 * which validates the topics with an exact match (equals).
 * 
 * @author Marco Luthardt (marco.luthardt@iandme.net)
 */
public class ExactTopicFilter implements TopicFilter {

    @Override
    public boolean validTopic(String eventTopic, String listenerTopic) {
        return eventTopic.equals(listenerTopic);
    }

}
