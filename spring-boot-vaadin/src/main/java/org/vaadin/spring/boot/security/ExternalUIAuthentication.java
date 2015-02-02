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
package org.vaadin.spring.boot.security;

import com.vaadin.ui.UI;

import java.lang.annotation.*;

/**
 * Annotation to be placed on Vaadin {@link com.vaadin.ui.UI}s that delegate to another Vaadin UI to handle the authentication.
 * Spring Security will automatically be configured to redirect to the URL of the specified authentication UI if
 * the current user has not been authenticated.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExternalUIAuthentication {

    /**
     * The UI that will handle the authentication. Spring Security will automatically be configured to allow access to the
     * UI if the current user is not authenticated.
     */
    Class<? extends UI> authenticationUI();
}
