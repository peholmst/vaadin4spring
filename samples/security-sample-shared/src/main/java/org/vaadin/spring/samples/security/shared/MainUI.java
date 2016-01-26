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
package org.vaadin.spring.samples.security.shared;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.samples.security.shared.views.AccessDeniedView;
import org.vaadin.spring.samples.security.shared.views.ErrorView;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

/**
 * Main application UI that allows the user to navigate between views, and log out.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Push(transport = Transport.WEBSOCKET)
public class MainUI extends UI {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    VaadinSecurity vaadinSecurity;

    @Autowired
    SpringViewProvider springViewProvider;

    @Autowired
    ValoSideBar sideBar;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Vaadin Shared Security Demo");
        // Let's register a custom error handler to make the 'access denied' messages a bit friendlier.
        setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
                    Notification.show("Sorry, you don't have access to do that.");
                } else {
                    super.error(event);
                }
            }
        });
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        // By adding a security item filter, only views that are accessible to the user will show up in the side bar.
        sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
        layout.addComponent(sideBar);

        CssLayout viewContainer = new CssLayout();
        viewContainer.setSizeFull();
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1f);

        Navigator navigator = new Navigator(this, viewContainer);
        // Without an AccessDeniedView, the view provider would act like the restricted views did not exist at all.
        springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.addProvider(springViewProvider);
        navigator.setErrorView(ErrorView.class);
        navigator.navigateTo(navigator.getState());

        setContent(layout); // Call this here because the Navigator must have been configured before the Side Bar can be attached to a UI.
    }
}
