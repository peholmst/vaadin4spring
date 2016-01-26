/*
 * Copyright 2016 The original authors
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

import javax.servlet.ServletContext;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereInterceptorAdapter;
import org.atmosphere.cpr.AtmosphereResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * An Atmosphere interceptor that populates the {@link SecurityContextHolder} with a security context
 * read from a {@link SecurityContextRepository}. The interceptor will attempt to use any repository defined
 * in the application context. If no repository is found, a new {@link HttpSessionSecurityContextRepository} is created
 * and used as-is.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class PushSecurityInterceptor extends AtmosphereInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushSecurityInterceptor.class);

    private SecurityContextRepository securityContextRepository;

    private synchronized SecurityContextRepository getSecurityContextRepository(ServletContext servletContext) {
        if (securityContextRepository == null) {
            final WebApplicationContext applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
            try {
                securityContextRepository = applicationContext.getBean(SecurityContextRepository.class);
            } catch (BeansException ex) {
                LOGGER.info(
                    "Found no SecurityContextRepository in the application context, using HttpSessionSecurityContextRepository");
                securityContextRepository = new HttpSessionSecurityContextRepository();
            }
        }
        return securityContextRepository;
    }

    @Override
    public Action inspect(AtmosphereResource r) {
        final SecurityContextRepository securityContextRepository = getSecurityContextRepository(
            r.getAtmosphereConfig().getServletContext());
        if (securityContextRepository.containsContext(r.getRequest())) {
            LOGGER.trace("Loading the security context from the session");
            final HttpRequestResponseHolder requestResponse = new HttpRequestResponseHolder(r.getRequest(),
                r.getResponse());
            final SecurityContext securityContext = securityContextRepository.loadContext(requestResponse);
            SecurityContextHolder.setContext(securityContext);
        }
        return Action.CONTINUE;
    }

    @Override
    public void postInspect(AtmosphereResource r) {
        LOGGER.trace("Clearing security context");
        SecurityContextHolder.clearContext();
    }
}
