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
package org.vaadin.spring.samples.security.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@VaadinUI
@Title("Vaadin Spring-Security Sample")
@Theme("security")
@Widgetset("org.vaadin.spring.samples.security.Widgetset")
public class MainUI extends UI {

    private static final long serialVersionUID = 5310014981075920878L;

    @Autowired
    private SpringViewProvider ViewProvider;
    
    @Override
    protected void init(VaadinRequest request) {
        
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(ViewProvider);
        setNavigator(navigator);
    }

}
