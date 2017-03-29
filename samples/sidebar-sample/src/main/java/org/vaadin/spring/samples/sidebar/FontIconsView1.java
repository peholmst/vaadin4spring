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

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

/**
 * Example view that demonstrates how to use a Vaadin Font Icon.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringView(name = FontIconsView1.VIEW_NAME)
@SideBarItem(sectionId = Sections.VAADIN_FONT_ICONS,
        caption = "View with Vaadin Font Icon",
        order = 1)
@VaadinFontIcon(VaadinIcons.ACCORDION_MENU)
@ViewScope
public class FontIconsView1 extends VerticalLayout implements View {

    public static final String VIEW_NAME = "viewWithVaadinFontIcon1";
    private static final long serialVersionUID = 2217814051618370412L;

    public FontIconsView1() {
        addComponent(new Label("View with Vaadin Font Icon 1"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        throw new UnsupportedOperationException();
    }
}
