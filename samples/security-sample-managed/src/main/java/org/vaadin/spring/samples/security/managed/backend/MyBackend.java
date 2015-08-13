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
package org.vaadin.spring.samples.security.managed.backend;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Example backend interface with {@link org.springframework.security.access.prepost.PreAuthorize} annotations.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface MyBackend {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String adminOnlyEcho(String s);

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    String echo(String s);
}
