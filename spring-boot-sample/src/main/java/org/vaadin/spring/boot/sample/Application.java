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
package org.vaadin.spring.boot.sample;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.navigator.VaadinView;

/**
 * Entry point into the Vaadin web application. You may run this from
 * {@code public static void main} or change the Maven {@code packaging} to
 * {@code war} and deploy to any Servlet 3 container, Java code unchanged.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 */
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

@VaadinUI
@Theme("sample")
@Title("Root UI")
class RootUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new CssLayout(new Label("Hello! I'm the root UI!"),
                new Link("Go to other UI", new ExternalResource("anotherUI"))));
    }
}

@VaadinUI(path = "/anotherUI")
@Widgetset("org.vaadin.spring.boot.sample.AppWidgetSet")
@Title("Another UI")
class AnotherUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;
    @Autowired
    ErrorView errorView;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Navigator navigator = new Navigator(this, this);
        navigator.setErrorView(errorView);
        navigator.addProvider(viewProvider);
        setNavigator(navigator);
    }
}

@VaadinView(name = "")
@UIScope
class MyDefaultView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addComponent(new Label(
                String.format("%s: It's %s and I was just entered!", 
                        getClass().getSimpleName(), new Date())));
        
        Chart chart = new Chart();
        chart.getConfiguration().addSeries(new ListSeries(1,2,3));
        
        addComponent(chart);
    }
}

@VaadinView(name = "myView", ui = AnotherUI.class)
@UIScope
class MyView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addComponent(new Label(String.format("%s: It's %s and I was just entered!", getClass().getSimpleName(), new Date())));
    }
}

@VaadinView(name = "hello/world", ui = AnotherUI.class)
@UIScope
class MyViewWithCustomName extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addComponent(new Label(String.format("%s: It's %s and I was just entered!", getClass().getSimpleName(), new Date())));
    }
}

@VaadinComponent
@UIScope
class ErrorView extends VerticalLayout implements View {

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
