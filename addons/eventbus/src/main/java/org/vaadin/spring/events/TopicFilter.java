package org.vaadin.spring.events;

public interface TopicFilter {

	boolean validTobic(String eventTopic, String listenerTopic);
	
}
