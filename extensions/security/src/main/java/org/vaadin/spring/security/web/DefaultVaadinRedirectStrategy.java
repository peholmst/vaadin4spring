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

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.UrlUtils;

import com.vaadin.ui.UI;

/**
 * Default implementation of {@link VaadinRedirectStrategy}. Based on
 * {@link org.springframework.security.web.DefaultRedirectStrategy}.
 *
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public class DefaultVaadinRedirectStrategy implements VaadinRedirectStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVaadinRedirectStrategy.class);

    private boolean contextRelative;

    @Autowired
    ServletContext servletContext;

    @Override
    public void sendRedirect(String url) {
        final String redirectUrl = calculateRedirectUrl(servletContext.getContextPath(), url);
        LOGGER.debug("Redirecting to [" + redirectUrl + "]");
        UI.getCurrent().getPage().setLocation(redirectUrl);
    }

    private String calculateRedirectUrl(String contextPath, String url) {
        if (!UrlUtils.isAbsoluteUrl(url)) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        // Full URL, including http(s)://

        if (!contextRelative) {
            return url;
        }

        // Calculate the relative URL from the fully qualified URL, minus the last
        // occurrence of the scheme and base context.
        url = url.substring(url.lastIndexOf("://") + 3); // strip off scheme
        url = url.substring(url.indexOf(contextPath) + contextPath.length());

        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }

    /**
     * If <tt>true</tt>, causes any redirection URLs to be calculated minus the protocol
     * and context path (defaults to <tt>false</tt>).
     */
    public void setContextRelative(boolean useRelativeContext) {
        this.contextRelative = useRelativeContext;
    }
}
