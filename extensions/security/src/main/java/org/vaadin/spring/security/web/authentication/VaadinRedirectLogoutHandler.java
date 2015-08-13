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
package org.vaadin.spring.security.web.authentication;

import org.vaadin.spring.security.web.VaadinRedirectStrategy;

/**
 * A logout handler that will redirect the user to a logout URL, that
 * will take care of the actual logging out.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinRedirectLogoutHandler implements VaadinLogoutHandler {

    private final VaadinRedirectStrategy redirectStrategy;
    private String logoutUrl = "/logout";

    public VaadinRedirectLogoutHandler(VaadinRedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    public VaadinRedirectLogoutHandler(VaadinRedirectStrategy redirectStrategy, String logoutUrl) {
        this.redirectStrategy = redirectStrategy;
        this.logoutUrl = logoutUrl;
    }

    /**
     * Returns the URL to redirect to, by default {@code /logout}.
     */
    public String getLogoutUrl() {
        return logoutUrl;
    }

    /**
     * Sets the URL to redirect to.
     */
    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    @Override
    public void onLogout() {
        redirectStrategy.sendRedirect(logoutUrl);
    }
}
