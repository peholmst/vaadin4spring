package org.vaadin.spring.events;

public interface TopicFilter {

    boolean validTopic(String eventTopic, String listenerTopic);

}
