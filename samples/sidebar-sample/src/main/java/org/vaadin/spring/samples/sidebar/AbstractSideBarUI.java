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
package org.vaadin.spring.samples.sidebar;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;

/**
 * Base class for side bar demonstration UIs.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class AbstractSideBarUI extends UI {

    private static final long serialVersionUID = -7747249047198990160L;

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Vaadin4Spring Side Bar Sample");
        final HorizontalLayout rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        setContent(rootLayout);

        final VerticalLayout viewContainer = new VerticalLayout();
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        rootLayout.addComponent(getSideBar());
        rootLayout.addComponent(viewContainer);
        rootLayout.setExpandRatio(viewContainer, 1.0f);
    }


    protected abstract AbstractSideBar getSideBar();

    private class ErrorView extends VerticalLayout implements View {

        private static final long serialVersionUID = -1349484555495574658L;
        private Label message;

        ErrorView() {
            setMargin(true);
            message = new Label();
            addComponent(message);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            message.setValue(String.format("No such view: %s", event.getViewName()));
        }
    }
}

