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

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Qualifier to be used to specify which type of {@link org.vaadin.spring.events.EventBus} to inject.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier
public @interface EventBusScope {
    /**
     * The scope of the event bus.
     */
    EventScope value();

    /**
     * Whether the event bus should be proxied or not. Typically, if you are injecting the event bus
     * into a singleton, you want this to be set to true. In all other cases, you want to use false.
     * Please note, that the {@link EventScope#APPLICATION} scoped event bus can never be proxied since it is
     * a singleton itself.
     */
    boolean proxy() default false;
}
