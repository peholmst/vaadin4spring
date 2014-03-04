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
package org.vaadin.spring.boot.sample.events;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.events.ApplicationEventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.SessionEventBus;
import org.vaadin.spring.events.UIEventBus;

/**
 * Demo of the scoped event buses.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUI(path = "/events")
public class EventsUI extends UI implements EventBusListener<Object> {

    @Autowired
    UIEventBus uiEventBus;

    @Autowired
    SessionEventBus sessionEventBus;

    @Autowired
    ApplicationEventBus applicationEventBus;

    @Autowired
    ApplicationContext applicationContext;

    private VerticalLayout layout;

    @Override
    protected void init(VaadinRequest request) {
        setPollInterval(300);
        /*
            We only need to subscribe to the UI event bus, since session scoped and application scoped events will propagate
            to it.
         */
        uiEventBus.subscribe(this);

        layout = new VerticalLayout(
                new Button("Publish UI Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        uiEventBus.publish(EventsUI.this, "Hello World from UI");
                    }
                }),
                new Button("Publish Session Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        sessionEventBus.publish(EventsUI.this, "Hello World from Session");
                    }
                }),
                new Button("Publish Application Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        applicationEventBus.publish(EventsUI.this, "Hello World from Application");
                    }
                }),
                new Button("Publish Application Context Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        applicationContext.publishEvent(new ApplicationEvent("Hello World from ApplicationContext") {
                            @Override
                            public Object getSource() {
                                return EventsUI.this;
                            }
                        });
                    }
                })
        );
        layout.setSpacing(true);
        setContent(layout);
    }

    @Override
    public void onEvent(org.vaadin.spring.events.Event<Object> event) {
        layout.addComponent(new Label(event.toString()));
    }
}
