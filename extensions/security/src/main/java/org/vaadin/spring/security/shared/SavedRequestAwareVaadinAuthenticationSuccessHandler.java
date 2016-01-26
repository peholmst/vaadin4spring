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
package org.vaadin.spring.security.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An authentication success strategy which can make use of the {@link VaadinRedirectStrategy} which may have been stored in
 * the session by the {@link org.springframework.security.web.access.ExceptionTranslationFilter}. When such a request is intercepted and requires authentication,
 * the request data is stored to record the original destination before the authentication process commenced, and to
 * allow the request to be reconstructed when a redirect to the same URL occurs. This class is responsible for
 * performing the redirect to the original URL if appropriate.
 * <p/>
 * Following a successful authentication, it decides on the redirect destination, based on the following scenarios:
 * <ul>
 * <li>
 * If the {@code alwaysUseDefaultTargetUrl} property is set to true, the {@code defaultTargetUrl}
 * will be used for the destination. Any {@code DefaultSavedRequest} stored in the session will be
 * removed.
 * </li>
 * <li>
 * If the {@code targetUrlParameter} has been set on the request, the value will be used as the destination.
 * Any {@code DefaultSavedRequest} will again be removed.
 * </li>
 * <li>
 * If a {@link SavedRequest} is found in the {@code RequestCache} (as set by the {@link org.springframework.security.web.access.ExceptionTranslationFilter} to
 * record the original destination before the authentication process commenced), a redirect will be performed to the
 * Url of that original destination. The {@code SavedRequest} object will remain cached and be picked up
 * when the redirected request is received.
 * </li>
 * <li>
 * If no {@code SavedRequest} is found, it will delegate to the base class.
 * </li>
 * </ul>
 * <p/>
 * Must be created as bean because to autowiring within parent class.
 *
 * @author Luke Taylor (original source code spring-security)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com) (Vaadin specific changes)
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SavedRequestAwareVaadinAuthenticationSuccessHandler extends VaadinUrlAuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    public SavedRequestAwareVaadinAuthenticationSuccessHandler(HttpService http, VaadinRedirectStrategy redirectStrategy, String defaultTargetUrl) {
        super(http, redirectStrategy, defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(Authentication authentication) throws Exception {

        HttpServletRequest request = http.getCurrentRequest();
        HttpServletResponse response = http.getCurrentResponse();

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(authentication);

            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(authentication);

            return;
        }

        clearAuthenticationAttributes();

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to saved request redirect url: " + targetUrl);
        redirectStrategy.sendRedirect(targetUrl);
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
