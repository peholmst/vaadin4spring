/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.security.context;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.ClassUtils;

import com.vaadin.server.VaadinSession;

/**
 * Class is based upon {@link org.springframework.security.web.context.HttpSessionSecurityContextRepository}
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public class VaadinSessionSecurityContextRepository implements SecurityContextRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Default key under which the security context will be stored in the session.
     */
    public static final String VAADIN_SECURITY_CONTEXT_KEY = "VAADIN_SECURITY_CONTEXT";
    
    /** SecurityContext instance used to check for equality with default (unauthenticated) content */
    private final Object contextObject = SecurityContextHolder.createEmptyContext();
    private boolean hasVaadinSession = false;
    private boolean allowSessionCreation = true;
    private boolean disableUrlRewriting = false;
    private boolean isServlet3 = ClassUtils.hasMethod(ServletRequest.class, "startAsync");
    private String springSecurityContextKey = VAADIN_SECURITY_CONTEXT_KEY;
    
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        
        /*
         * Try to obtain the Request/Response from the VaadinSession
         */
        logger.debug("CUSTOM VAADIN SESSION");
        
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        
        if ( vaadinSession != null ) {
            // VaadinSession Present
            logger.debug("HAS VAADIN SESSION OBJECT");
            hasVaadinSession = true;
        } else {
            logger.debug("NO VAADIN SESSION");
            HttpSession httpSession = request.getSession(false);
        }

        //SecurityContext context = readSecurityContextFromSession(httpSession);
/*
        if (context == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No SecurityContext was available from the HttpSession: " + httpSession +". " +
                        "A new one will be created.");
            }
            context = null;

        }
*/
        //SaveToSessionResponseWrapper wrappedResponse = new SaveToSessionResponseWrapper(response, request, httpSession != null, context);
        //requestResponseHolder.setResponse(wrappedResponse);

        if(isServlet3) {
            //requestResponseHolder.setRequest(new Servlet3SaveToSessionRequestWrapper(request, wrappedResponse));
        }

        return null;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }
}
