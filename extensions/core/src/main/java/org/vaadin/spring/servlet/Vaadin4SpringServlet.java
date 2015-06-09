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

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.server.SpringVaadinServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;

/**
 * An extended version of {@link com.vaadin.spring.server.SpringVaadinServlet} that provides the following additional features:
 * <ul>
 * <li>Support for specifying a custom {@link com.vaadin.server.SystemMessagesProvider} by making it available as a Spring managed bean</li>
 * <li>Support for {@link org.vaadin.spring.request.VaadinRequestStartListener}s and {@link org.vaadin.spring.request.VaadinRequestEndListener}s</li>
 * </ul>
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.servlet.Vaadin4SpringServletService
 */
public class Vaadin4SpringServlet extends SpringVaadinServlet {

    private static final Logger logger = LoggerFactory.getLogger(Vaadin4SpringServlet.class);

    public Vaadin4SpringServlet() {
        logger.info("Using custom Vaadin4Spring servlet");
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        final Vaadin4SpringServletService service = new Vaadin4SpringServletService(this, deploymentConfiguration, getServiceUrlPath());
        service.init();
        return service;
    }

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        final WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        try {
            SystemMessagesProvider systemMessagesProvider = applicationContext.getBean(SystemMessagesProvider.class);
            logger.info("Using custom SystemMessagesProvider {}", systemMessagesProvider);
            getService().setSystemMessagesProvider(systemMessagesProvider);
        } catch (BeansException ex) {
            logger.info("Could not find a SystemMessagesProvider in the application context, using default");
            logger.trace("Exception thrown while looking for a SystemMessagesProvider", ex);
        }
    }
}
