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
 * This annotation can be placed on fields or getter methods whose types are JavaBean with String properties that are to be
 * translated. For example:
 * <pre>
 * &#64;TranslatedProperty(property = "value", key = "myLabel.value")
 * private Label myLabel;
 *
 * &#64;TranslatedProperty(property = "description", key = "myTextField.description")
 * public TextField getMyTextField() { ... }
 * </pre>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TranslatedProperty {

    /**
     * The name of the JavaBean String property.
     */
    String property();

    /**
     * The name of the key to pass to the {@link org.springframework.context.MessageSource} when looking up the translated string.
     */
    String key();

    /**
     * The default value to use if the message is not found in the {@link org.springframework.context.MessageSource}.
     */
    String defaultValue() default "";
}
