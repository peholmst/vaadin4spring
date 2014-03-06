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
package org.vaadin.spring.i18n;

import java.lang.annotation.*;

/**
 * Annotation that makes it possible to place multiple {@link TranslatedProperty} annotations on the same element. For example:
 * <code>
 * <pre>
 * &#64;TranslatedProperties({
 *     &#64;TranslatedProperty(property = "caption", key = "myTextField.caption"),
 *     &#64;TranslatedProperty(property = "description", key = "myTextField.description")
 * })
 * private TextField myTextField;
 * </pre>
 * </code>
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.i18n.TranslatedProperty
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TranslatedProperties {

    TranslatedProperty[] value();
}
