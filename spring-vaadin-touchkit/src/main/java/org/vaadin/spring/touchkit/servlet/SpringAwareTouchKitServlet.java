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
package org.vaadin.spring.touchkit.servlet;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;

/**
 * Subclass of {@link com.vaadin.addon.touchkit.server.TouchKitServlet} that adds a {@link org.vaadin.spring.touchkit.servlet.SpringAwareTouchKitUIProvider} to
 * every new Vaadin session.
 * <p/>
 * If you need a custom TouchKit servlet, you can either extend this servlet directly, or extend another subclass of {@link com.vaadin.addon.touchkit.server.TouchKitServlet}
 * and just add the UI provider.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SpringAwareTouchKitServlet extends TouchKitServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                SpringAwareTouchKitUIProvider uiProvider = new SpringAwareTouchKitUIProvider(webApplicationContext);
                sessionInitEvent.getSession().addUIProvider(uiProvider);
            }
        });
    }
}
