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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.stuff.sidebar.SideBarItem;

/**
 * Example view that shows up under the Planning section in the side bar.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinView(name = PlanningView1.VIEW_NAME)
@SideBarItem(sectionId = Sections.PLANNING,
        caption = "View 1",
        iconResource = "../runo/icons/64/calendar.png",
        order = 1)
@UIScope
public class PlanningView1 extends VerticalLayout implements View {

    public static final String VIEW_NAME = "planning1";

    public PlanningView1() {
        addComponent(new Label("Planning View 1"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
