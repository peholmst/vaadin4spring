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
package org.vaadin.spring.stuff.i18n;

import org.springframework.context.annotation.Import;
import org.vaadin.spring.stuff.i18n.config.CompositeMessageSourceConfiguration;

import java.lang.annotation.*;

/**
 * Add this annotation to your application configuration to enable the {@link org.vaadin.spring.stuff.i18n.CompositeMessageSource}.
 * Please note that you also have to define {@link org.vaadin.spring.stuff.i18n.MessageProvider}s that can feed the message source with messages.
 * You can use {@link org.vaadin.spring.stuff.i18n.ResourceBundleMessageProvider} or implement your own.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CompositeMessageSourceConfiguration.class)
public @interface EnableCompositeMessageSource {
}
