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

import java.lang.annotation.*;

/**
 * Annotation to be placed on event bus listener methods. A listener method must always conform to the following method signature:
 * <code>myMethodName({@link org.vaadin.spring.events.Event Event}&lt;myPayloadType&gt;)</code>.
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
}
