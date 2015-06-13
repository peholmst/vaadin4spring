package org.vaadin.spring.events;

public class ExactTopicFilter implements TopicFilter {

    @Override
    public boolean validTopic(String eventTopic, String listenerTopic) {
        return eventTopic.equals(listenerTopic);
    }

}
