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

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceDestroyListener;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.SystemMessagesProvider;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.spring.SpringServlet;

/**
 * An extended version of {@link com.vaadin.spring.server.SpringVaadinServlet} that provides the following additional
 * features:
 * <ul>
 * <li>Support for specifying a custom {@link com.vaadin.server.SystemMessagesProvider} by making it available as a
 * Spring managed bean</li>
 * <li>Support for adding {@link com.vaadin.server.SessionInitListener}s by making them available as Spring managed
 * beans</li>
 * <li>Support for adding {@link com.vaadin.server.SessionDestroyListener}s by making them available as Spring managed
 * beans</li>
 * <li>Support for adding {@link com.vaadin.server.ServiceDestroyListener}s by making them available as Spring managed
 * beans</li>
 * <li>Support for {@link org.vaadin.spring.request.VaadinRequestStartListener}s and
 * {@link org.vaadin.spring.request.VaadinRequestEndListener}s</li>
 * <li>Support for providing custom init parameters by Spring managed {@link CustomInitParameterProvider}s.</li>
 * </ul>
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.servlet.Vaadin4SpringServletService
 */
public class Vaadin4SpringServlet extends SpringServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vaadin4SpringServlet.class);

    public Vaadin4SpringServlet(ApplicationContext context,
            boolean forwardingEnforced) {
    	super(context, forwardingEnforced);
        LOGGER.info("Using custom Vaadin4Spring servlet");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(new ServletConfigWrapper(servletConfig));
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
        throws ServiceException {
        final Vaadin4SpringServletService service = new Vaadin4SpringServletService(this, deploymentConfiguration);
        service.init();
        return service;
    }

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        final WebApplicationContext applicationContext = WebApplicationContextUtils
            .getWebApplicationContext(getServletContext());
        try {
            SystemMessagesProvider systemMessagesProvider = applicationContext.getBean(SystemMessagesProvider.class);
            LOGGER.info("Using custom SystemMessagesProvider {}", systemMessagesProvider);
            getService().setSystemMessagesProvider(systemMessagesProvider);
        } catch (BeansException ex) {
            LOGGER.info("Could not find a SystemMessagesProvider in the application context, using default");
        }
        for (SessionInitListener sessionInitListener : applicationContext.getBeansOfType(SessionInitListener.class)
            .values()) {
            LOGGER.info("Adding SessionInitListener {}", sessionInitListener);
            getService().addSessionInitListener(sessionInitListener);
        }
        for (SessionDestroyListener sessionDestroyListener : applicationContext
            .getBeansOfType(SessionDestroyListener.class).values()) {
            LOGGER.info("Adding SessionDestroyListener {}", sessionDestroyListener);
            getService().addSessionDestroyListener(sessionDestroyListener);
        }
        for (ServiceDestroyListener serviceDestroyListener : applicationContext
            .getBeansOfType(ServiceDestroyListener.class).values()) {
            LOGGER.info("Adding ServiceDestroyListener {}", serviceDestroyListener);
            getService().addServiceDestroyListener(serviceDestroyListener);
        }
        LOGGER.info("Custom Vaadin4Spring servlet initialization completed");
    }

    private static class ServletConfigWrapper implements ServletConfig {

        private final ServletConfig delegate;

        private final Set<CustomInitParameterProvider> customInitParameterProviders;

        private ServletConfigWrapper(ServletConfig delegate) {
            this.delegate = delegate;
            final WebApplicationContext applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(delegate.getServletContext());
            customInitParameterProviders = new HashSet<CustomInitParameterProvider>(
                applicationContext.getBeansOfType(CustomInitParameterProvider.class).values());
            LOGGER.info("Found {} custom init parameter provider(s)", customInitParameterProviders.size());
        }

        @Override
        public String getServletName() {
            return delegate.getServletName();
        }

        @Override
        public ServletContext getServletContext() {
            return delegate.getServletContext();
        }

        @Override
        public String getInitParameter(String name) {
            for (CustomInitParameterProvider provider : customInitParameterProviders) {
                if (provider.containsInitParameter(name)) {
                    LOGGER.trace("Found custom init parameter [{}] in provider [{}]", name, provider);
                    return provider.getInitParameter(name);
                }
            }
            LOGGER.trace("No custom init parameter named [{}] found, delegating to original ServletConfig", name);
            return delegate.getInitParameter(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            Set<String> initParameterNames = new HashSet<String>();
            Enumeration<String> delegateInitParameterNames = delegate.getInitParameterNames();
            while (delegateInitParameterNames.hasMoreElements()) {
                initParameterNames.add(delegateInitParameterNames.nextElement());
            }
            for (CustomInitParameterProvider provider : customInitParameterProviders) {
                initParameterNames.addAll(provider.getInitParameterNames());
            }
            LOGGER.trace("Init parameter names are {}", initParameterNames);
            return new Vector<String>(initParameterNames).elements();
        }
    }
}
