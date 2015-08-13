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
import org.vaadin.spring.security.config.VaadinManagedSecurityConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables managed security for your Vaadin application. This means that Vaadin will manage the {@link org.springframework.security.core.context.SecurityContext}
 * and take care of login and logout by using {@link org.vaadin.spring.security.VaadinSecurity}. No Spring Web Security should be enabled
 * for the Vaadin application's URLs <strong>at all</strong>. Global method security is enabled by default, which means that the backend and the {@link com.vaadin.navigator.View}s
 * can be protected using either {@link org.springframework.security.access.prepost.PreAuthorize} or {@link org.springframework.security.access.annotation.Secured} annotations.
 * <p/>
 * Use Vaadin managed Spring Security when your application is using a Vaadin UI only and you want to control the logins and logouts yourself. Push with web sockets is also supported
 * in this mode.
 *
 * @see org.vaadin.spring.security.config.AuthenticationManagerConfigurer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(VaadinManagedSecurityConfiguration.class)
public @interface EnableVaadinManagedSecurity {
}
