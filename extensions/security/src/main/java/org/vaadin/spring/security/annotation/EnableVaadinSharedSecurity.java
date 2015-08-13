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
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables shared security for your Vaadin application. This means that Vaadin will participate in Spring Security as an ordinary web application, letting Spring
 * Security handle login, logout, {@link org.springframework.security.core.context.SecurityContext} management, etc. When using shared security, you have to manually
 * configure Spring Security just like you would in a normal Spring-based web application. Vaadin4Spring provides some additional services and helper methods to
 * make this easier. Please note that push with web sockets is not currently supported in this mode.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(VaadinSharedSecurityConfiguration.class)
public @interface EnableVaadinSharedSecurity {
}
