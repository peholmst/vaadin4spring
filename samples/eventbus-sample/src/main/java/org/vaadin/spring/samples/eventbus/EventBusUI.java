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
package org.vaadin.spring.samples.eventbus;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;

import javax.annotation.PreDestroy;

/**
 * Demo of the scoped event buses.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUI
@Push(transport = Transport.LONG_POLLING)
public class EventBusUI extends UI implements EventBusListener<Object> {

    @Autowired
    EventBus eventBus;

    @Autowired
    ApplicationContext applicationContext;

    private VerticalLayout layout;

    @Override
    protected void init(VaadinRequest request) {
        eventBus.subscribe(this);

        layout = new VerticalLayout(
                new Button("Publish UI Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        eventBus.publish(EventBusUI.this, "Hello World from UI");
                    }
                }),
                new Button("Publish Session Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        eventBus.publish(EventScope.SESSION, EventBusUI.this, "Hello World from Session");
                    }
                }),
                new Button("Publish Application Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        eventBus.publish(EventScope.APPLICATION, EventBusUI.this, "Hello World from Application");
                    }
                }),
                new Button("Publish Application Context Event", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        applicationContext.publishEvent(new ApplicationEvent("Hello World from ApplicationContext") {
                            @Override
                            public Object getSource() {
                                return EventBusUI.this;
                            }
                        });
                    }
                })
        );
        layout.setSpacing(true);
        setContent(layout);
    }

    @Override
    public void onEvent(final org.vaadin.spring.events.Event<Object> event) {
        getUI().access(new Runnable() {
            @Override
            public void run() {
                layout.addComponent(new Label(event.toString()));
            }
        });
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }
}
