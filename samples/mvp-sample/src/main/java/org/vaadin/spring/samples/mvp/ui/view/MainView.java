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
package org.vaadin.spring.samples.mvp.ui.view;

import javax.annotation.PostConstruct;

import org.vaadin.spring.navigator.annotation.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinUIScope
@VaadinView(name = MainView.NAME)
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = 6041151920926888211L;
    public static final String NAME = "main";


    @PostConstruct
    private void init() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }


    public void setBanner(Component banner) {
        addComponent(banner);
    }

    public void setHeader(Component header) {
        addComponent(header);
        setExpandRatio(header, 1);
    }

    public void setBody(Component body) {
        addComponent(body);
        setExpandRatio(body, 10);
    }

    public void setFooter(Component footer) {
        addComponent(footer);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
