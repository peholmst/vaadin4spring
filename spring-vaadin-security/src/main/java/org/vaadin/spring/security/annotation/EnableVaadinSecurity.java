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
package org.vaadin.spring.security.annotation;

import org.springframework.context.annotation.Import;
import org.vaadin.spring.security.config.VaadinSecurityConfiguration;

import java.lang.annotation.*;

/**
 * Addition to the {@link org.vaadin.spring.annotation.EnableVaadin} annotation that configures support for Spring Security in Vaadin applications.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.annotation.EnableVaadin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(VaadinSecurityConfiguration.class)
public @interface EnableVaadinSecurity {
}
