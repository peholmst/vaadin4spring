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
package org.vaadin.spring.security.web;

import org.springframework.security.web.RedirectStrategy;

/**
 * Encapsulates the redirection logic for all classes in the framework which perform redirects.
 * <br><br>
 * Based upon concept of Spring Security {@link RedirectStrategy}
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
public interface VaadinRedirectStrategy {

    /**
     * Performs a redirect to the supplied URL
     * @param url the target URL to redirect to, for example "/login"
     */
    void sendRedirect(String url);
}
