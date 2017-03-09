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

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.v7.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.AccordionSideBar;

/**
 * UI that demonstrates the {@link org.vaadin.spring.sidebar.components.AccordionSideBar}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI(path = "/accordion")
@Theme(Reindeer.THEME_NAME)
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class AccordionSideBarUI extends AbstractSideBarUI {

    private static final long serialVersionUID = -7747249047198990160L;

    @Autowired
    AccordionSideBar sideBar;

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }
}

