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
package org.vaadin.spring.security.internal;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.request.VaadinRequestEndListener;
import org.vaadin.spring.request.VaadinRequestStartListener;

/**
 * A request listener that manages the {@link org.springframework.security.core.context.SecurityContextHolder}.
 * <ul>
 * <li>When a request is started, the security context will be retrieved from the Vaadin Session if available.</li>
 * <li>When a request is ended, the security context will be stored back into the Vaadin Session and cleared.</li>
 * </ul>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SecurityContextVaadinRequestListener implements VaadinRequestStartListener, VaadinRequestEndListener {

    /**
     * The name of the Vaadin Session attribute that contains the security context.
     */
    public static final String SECURITY_CONTEXT_SESSION_ATTRIBUTE = "org.vaadin.spring.security.internal.springSecurityContext";
    private static final Logger logger = LoggerFactory.getLogger(SecurityContextVaadinRequestListener.class);

    public SecurityContextVaadinRequestListener() {
        logger.info("Initializing {}, setting SecurityContextHolder strategy to {}", getClass().getSimpleName(), SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    public void onRequestStart(VaadinRequest request, VaadinResponse response) {
        final WrappedSession wrappedSession = request.getWrappedSession(false);
        VaadinSession session = null;
        if (wrappedSession != null) {
            session = VaadinSession.getForSession(request.getService(), wrappedSession);
        }

        SecurityContextHolder.clearContext();
        if (session != null) {
            logger.debug("Loading security context from VaadinSession {}", session);
            SecurityContext securityContext;
            session.lock();
            try {
                securityContext = (SecurityContext) session.getAttribute(SECURITY_CONTEXT_SESSION_ATTRIBUTE);
            } finally {
                session.unlock();
            }
            if (securityContext == null) {
                logger.debug("No security context found in VaadinSession {}", session);
            } else {
                logger.debug("Setting security context to {}", securityContext);
                SecurityContextHolder.setContext(securityContext);
            }
        } else {
            logger.debug("No VaadinSession available for retrieving the security context");
        }
    }

    @Override
    public void onRequestEnd(VaadinRequest request, VaadinResponse response, VaadinSession session) {
        try {
            if (session != null) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                logger.debug("Storing security context {} in VaadinSession {}", securityContext, session);
                session.lock();
                try {
                    session.setAttribute(SECURITY_CONTEXT_SESSION_ATTRIBUTE, securityContext);
                } finally {
                    session.unlock();
                }
            } else {
                logger.debug("No VaadinSession available for storing the security context");
            }
        } finally {
            logger.debug("Clearing security context");
            SecurityContextHolder.clearContext();
        }
    }
}
