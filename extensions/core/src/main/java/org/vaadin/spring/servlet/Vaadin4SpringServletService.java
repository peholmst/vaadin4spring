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
package org.vaadin.spring.servlet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vaadin.spring.request.VaadinRequestEndListener;
import org.vaadin.spring.request.VaadinRequestStartListener;

import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.SpringVaadinServletService;

/**
 * Extended version of {@link com.vaadin.flow.spring.SpringVaadinServletService} that adds support
 * for {@link VaadinRequestStartListener}s and {@link org.vaadin.spring.request.VaadinRequestEndListener}s.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class Vaadin4SpringServletService extends SpringVaadinServletService {

    private static final Logger logger = LoggerFactory.getLogger(Vaadin4SpringServletService.class);
    private final WebApplicationContext applicationContext;

    /**
     * Create a servlet service instance that allows the use of a custom service
     * URL.
     *
     * @param servlet
     * @param deploymentConfiguration
     * @param serviceUrl              custom service URL to use (relative to context path, starting
     *                                with a slash) or null for default
     * @throws ServiceException
     */
    public Vaadin4SpringServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        super(servlet, deploymentConfiguration, WebApplicationContextUtils.getWebApplicationContext(servlet.getServletContext()));
        this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servlet.getServletContext());
        logger.info("Using custom Vaadin4Spring servlet service");
//        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servlet.getServletContext());
    }

    @Override
    public void requestStart(VaadinRequest request, VaadinResponse response) {
        super.requestStart(request, response);
        logger.trace("Invoking VaadinRequestStartListeners");
        for (VaadinRequestStartListener listener : applicationContext.getBeansOfType(VaadinRequestStartListener.class).values()) {
            try {
                listener.onRequestStart(request, response);
            } catch (Exception ex) {
                logger.error("VaadinRequestStartListener threw an exception, ignoring", ex);
            }
        }
        logger.trace("Finished invoking VaadinRequestStartListeners");
    }

    @Override
    public void requestEnd(VaadinRequest request, VaadinResponse response, VaadinSession session) {
        logger.trace("Invoking VaadinRequestEndListeners");
        for (VaadinRequestEndListener listener : applicationContext.getBeansOfType(VaadinRequestEndListener.class).values()) {
            try {
                listener.onRequestEnd(request, response, session);
            } catch (Exception ex) {
                logger.error("VaadinRequestEndListener threw an exception, ignoring", ex);
            }
        }
        logger.trace("Finished invoking VaadinRequestEndListener");
        super.requestEnd(request, response, session);
    }
}
