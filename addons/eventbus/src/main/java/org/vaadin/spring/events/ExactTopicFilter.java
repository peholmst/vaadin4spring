package org.vaadin.spring.events;

public class ExactTopicFilter implements TopicFilter {

	@Override
	public boolean validTobic(String eventTopic, String listenerTopic) {
		return eventTopic.equals(listenerTopic);
	}

}
