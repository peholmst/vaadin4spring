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

import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventBusListenerMethodFilter;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.NoEventBusListenerMethodFilter;

/**
 * Annotation to be placed on event bus listener methods. A listener method must always conform to one of the following method signatures:
 * <ol>
 *   <li><code>myMethodName({@link org.vaadin.spring.events.Event Event}&lt;MyPayloadType&gt;)</code></li>
 *   <li><code>myMethodName(MyPayloadType)</code></li>
 * </ol>
 * A listener method can have any visibility and any return type.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.events.EventBusListener
 * @see EventBus#subscribe(Object)
 * @see EventBus#subscribe(Object, boolean)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusListenerMethod {

    /**
     * The default scope of a listener method is <code>EventScope.UNDEFINED</code>
     * This means that listener will listen for any {@link EventScope} if {@link EventBus#subscribe(EventBusListener, boolean)} is set to propagate event
     */
    EventScope scope() default EventScope.UNDEFINED;

    Class<? extends EventBusListenerMethodFilter> filter() default NoEventBusListenerMethodFilter.class;
    
    /**
     * Filter by source class 
     */
    Class<?>[] source() default {};
}
