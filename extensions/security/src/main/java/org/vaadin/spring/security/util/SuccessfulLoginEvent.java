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
package org.vaadin.spring.security.util;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

import com.vaadin.flow.component.UI;

/**
 * Event that can be published using either the Spring event publisher or the Vaadin4Spring event bus when a user
 * logs in successfully. See the <b>security-sample-managed</b> sample application for an example of how it can be used.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SuccessfulLoginEvent extends ApplicationEvent {

    private final Authentication authentication;

    /**
     * Creates a new event.
     *
     * @param source         the UI instance in which the user logged in.
     * @param authentication the authentication token returned by the Spring {@link org.springframework.security.authentication.AuthenticationManager}.
     */
    public SuccessfulLoginEvent(UI source, Authentication authentication) {
        super(source);
        this.authentication = authentication;
    }

    /**
     * Returns the authentication token of the user.
     */
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public UI getSource() {
        return (UI) super.getSource();
    }
}
