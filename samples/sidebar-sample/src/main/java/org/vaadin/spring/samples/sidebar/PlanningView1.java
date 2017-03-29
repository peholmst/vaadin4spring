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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * Example view that shows up under the Planning section in the side bar. This view also demonstrates the
 * behavior of the view scope.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringView(name = PlanningView1.VIEW_NAME)
@SideBarItem(sectionId = Sections.PLANNING,
        caption = "View 1",
        order = 1)
@FontAwesomeIcon(FontAwesome.ANDROID)
@ViewScope
public class PlanningView1 extends VerticalLayout implements View {

    public static final String VIEW_NAME = "planning1";
    private static final long serialVersionUID = 2217814051618370412L;

    public PlanningView1() {
        addComponent(new Label("Planning View 1"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        throw new UnsupportedOperationException();
    }
}
