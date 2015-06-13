package org.vaadin.spring.events.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vaadin.spring.events.ExactTopicFilter;
import org.vaadin.spring.events.TopicFilter;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusListenerTopic {

    /**
     * A topic is a String which can be specified while publishing an event. The
     * method will only called when the topic matches the given String in the
     * published method.
     */
    String topic() default "";

    Class<? extends TopicFilter> filter() default ExactTopicFilter.class;

}
