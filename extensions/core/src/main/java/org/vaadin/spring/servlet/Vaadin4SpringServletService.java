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

import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.SpringVaadinServletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.request.VaadinRequestEndListener;
import org.vaadin.spring.request.VaadinRequestStartListener;

/**
 * Extended version of {@link SpringVaadinServletService} that adds support
 * for {@link org.vaadin.spring.request.VaadinRequestStartListener}s and {@link org.vaadin.spring.request.VaadinRequestEndListener}s.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class Vaadin4SpringServletService extends SpringVaadinServletService {

    private static final Logger logger = LoggerFactory.getLogger(Vaadin4SpringServletService.class);

    private final ApplicationContext context;

    /**
     * Creates an instance connected to the given servlet and using the given
     * configuration with provided application {@code context}.
     *
     * @param servlet
     *         the servlet which receives requests
     * @param deploymentConfiguration
     *         the configuration to use
     * @param context
     *         the Spring application context
     */
    public Vaadin4SpringServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration, ApplicationContext context) {
        super(servlet, deploymentConfiguration, context);
        this.context = context;
        logger.info("Using custom Vaadin4Spring servlet service");
    }

    @Override
    public void requestStart(VaadinRequest request, VaadinResponse response) {
        super.requestStart(request, response);
        logger.trace("Invoking VaadinRequestStartListeners");
        for (VaadinRequestStartListener listener : context.getBeansOfType(VaadinRequestStartListener.class).values()) {
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
        for (VaadinRequestEndListener listener : context.getBeansOfType(VaadinRequestEndListener.class).values()) {
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
