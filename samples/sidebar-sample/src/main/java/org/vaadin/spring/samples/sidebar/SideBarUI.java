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
package org.vaadin.spring.samples.sidebar;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.stuff.sidebar.SideBar;

/**
 * UI that demonstrates the {@link org.vaadin.spring.stuff.sidebar.SideBar}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUI
public class SideBarUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;

    @Autowired
    SideBar sideBar;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Vaadin4Spring Side Bar Sample");
        final HorizontalSplitPanel rootLayout = new HorizontalSplitPanel();
        rootLayout.setStyleName(Reindeer.SPLITPANEL_SMALL);
        rootLayout.setSizeFull();
        setContent(rootLayout);

        final Navigator navigator = new Navigator(this, new ViewDisplay() {
            @Override
            public void showView(View view) {
                System.out.println("Showing view " + view);
                rootLayout.setSecondComponent((com.vaadin.ui.Component) view);
            }
        });
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        rootLayout.setFirstComponent(sideBar);
        rootLayout.setSplitPosition(150, Unit.PIXELS);
    }

    private class ErrorView extends VerticalLayout implements View {
        private Label message;

        ErrorView() {
            setMargin(true);
            addComponent(message = new Label());
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            message.setValue(String.format("No such view: %s", event.getViewName()));
        }
    }
}

