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
package org.vaadin.spring.security.config;

import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.internal.SharedVaadinSecurityImpl;

/**
 * Configuration for setting up Vaadin shared Spring Security. See {@link org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity} for details.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Configuration
public class VaadinSharedSecurityConfiguration extends AbstractVaadinSecurityConfiguration {

    @Override
    VaadinSecurity vaadinSecurity() {
        // TODO Add support for configuring SharedVaadinSecurityImpl upon creation
        return new SharedVaadinSecurityImpl();
    }
}
