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

import java.lang.annotation.*;

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
}
