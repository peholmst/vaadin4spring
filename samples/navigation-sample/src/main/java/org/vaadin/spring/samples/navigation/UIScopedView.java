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
package org.vaadin.spring.samples.navigation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Example of a view that uses the {@link org.vaadin.spring.annotation.VaadinUIScope}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUIScope
@VaadinView(name = UIScopedView.VIEW_NAME)
public class UIScopedView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ui";
    private static final Logger LOGGER = LoggerFactory.getLogger(UIScopedView.class);
    @Autowired
    UIScopedBusinessObject uiScopedBusinessObject; // You can inject other UI scoped beans into a UI scoped bean

    @PostConstruct
    void init() {
        LOGGER.info("I'm being created: {}", this);
        setMargin(true);
        setSpacing(true);

        final Label label = new Label(String.format("This is a UI scoped view. The same instance is used every time this view is shown. " +
                "This particular instance is <b>%s</b>. If you navigate away from this view and back, you'll notice that the instance remains the same.", this));
        label.setContentMode(ContentMode.HTML);
        addComponent(label);

        addComponent(new Button("Invoke a UI scoped business object", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addComponent(new Label(uiScopedBusinessObject.sayHello()));
            }
        }));
    }

    @PreDestroy
    void destroy() {
        // This method will get called when the UI is destroyed. If you want to try it out, wait for the session to expire
        // and check the logs.
        LOGGER.info("I'm being destroyed: {}", this);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
