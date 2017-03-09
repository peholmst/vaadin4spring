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
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 * UI that demonstrates the {@link org.vaadin.spring.sidebar.components.ValoSideBar}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI(path = "/valo")
@Theme("sidebar") // A custom theme based on Valo
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class ValoSideBarUI extends AbstractSideBarUI {

    @Autowired
    ValoSideBar sideBar;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);
        CssLayout header = new CssLayout();

        MenuBar menuBar = new MenuBar();
        header.addComponent(menuBar);

        MenuBar.MenuItem settingsItem = menuBar.addItem("", FontAwesome.WRENCH, null);

        MenuBar.MenuItem useLargeIconsItem = settingsItem.addItem("Use large icons", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                sideBar.setLargeIcons(selectedItem.isChecked());
            }
        });
        useLargeIconsItem.setCheckable(true);

        MenuBar.MenuItem showLogoItem = settingsItem.addItem("Show logo", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                if (selectedItem.isChecked()) {
                    showLogo();
                } else {
                    hideLogo();
                }
            }
        });
        showLogoItem.setCheckable(true);

        sideBar.setHeader(header);
    }

    private void showLogo() {
        sideBar.setLogo(new Label(FontAwesome.ROCKET.getHtml(), ContentMode.HTML));
    }

    private void hideLogo() {
        sideBar.setLogo(null);
    }

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }
}
