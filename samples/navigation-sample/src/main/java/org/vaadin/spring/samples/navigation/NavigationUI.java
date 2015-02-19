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
package org.vaadin.spring.samples.navigation;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

import javax.annotation.PostConstruct;

/**
 * Main UI of the navigation sample UI. The UI contains three different views with different scopes. The user
 * can navigate between the views by clicking on buttons on a navigation bar at the top of the window.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUI
@Theme(ValoTheme.THEME_NAME)
public class NavigationUI extends UI {

    private final SpringViewProvider viewProvider;

    @Autowired
    public NavigationUI(SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("Prototype Scoped View", PrototypeScopedView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("UI Scoped View", UIScopedView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("View Scoped View", ViewScopedView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Access Control", AccessControlView.VIEW_NAME));
        root.addComponent(navigationBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.setErrorView(new ErrorView()); // You can still create the error view yourself if you want to.
        navigator.addProvider(viewProvider);
    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getNavigator().navigateTo(viewName);
            }
        });
        return button;
    }

    private class ErrorView extends VerticalLayout implements View {

        private Label message;

        ErrorView() {
            setMargin(true);
            addComponent(message = new Label("Please click one of the buttons at the top of the screen."));
            message.addStyleName(ValoTheme.LABEL_COLORED);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
        }
    }

    @VaadinComponent
    @PrototypeScope
    public static class AccessDeniedView extends VerticalLayout implements View {

        private Label message;

        @PostConstruct
        void init() {
            setMargin(true);
            addComponent(message = new Label());
            message.addStyleName(ValoTheme.LABEL_FAILURE);
            message.setContentMode(ContentMode.HTML);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            message.setValue(String.format("Sorry, but you don't have access to the view <b>%s</b>.", event.getViewName()));
        }
    }
}
