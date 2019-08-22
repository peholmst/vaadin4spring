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
package org.vaadin.spring.events.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vaadin.spring.events.ExactTopicFilter;
import org.vaadin.spring.events.TopicFilter;

/**
 * Annotation to be placed on event bus listener methods, additional to
 * the {@link org.vaadin.spring.events.annotation.EventBusListenerMethod} annotation.
 * A topic is specified as string which will be defined when publishing an event.
 * Each method annotated with this annotation and the corresponding topic will be
 * called as listener.
 * 
 * Topics can be filtered with implementations of the {@link org.vaadin.spring.events.TopicFilter} interface.
 * 
 * @author Marco Luthardt (marco.luthardt@iandme.net)
 * @see org.vaadin.spring.events.annotation.EventBusListenerMethod
 * @see org.vaadin.spring.events.TopicFilter
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusListenerTopic {

    /**
     * A topic is a string which can be specified while publishing an event. The
     * method will only called when the topic matches the given String in the
     * published method.
     */
    String topic() default "";

    /**
     * The filter to be used to validate the published and listener topic.
     * 
     * @return an implementation of the {@link org.vaadin.spring.events.TopicFilter} interface.
     */
    Class<? extends TopicFilter> filter() default ExactTopicFilter.class;

}
