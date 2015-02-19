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

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.navigator.annotation.VaadinView;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


/**
 * A view that demonstrates how {@link org.vaadin.spring.navigator.SpringViewProvider.ViewProviderAccessDelegate}s can be used
 * to control access to views. In this example, the access delegate is the UI scoped view, but you can also use e.g. singleton
 * access delegates.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUIScope
@VaadinView(name = AccessControlView.VIEW_NAME)
public class AccessControlView extends VerticalLayout implements View, SpringViewProvider.ViewProviderAccessDelegate {

    public static final String VIEW_NAME = "access";

    private final Set<String> allowedViews = new HashSet<>();
    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    void init() {
        allowedViews.add(VIEW_NAME);
        allowedViews.add(PrototypeScopedView.VIEW_NAME);
        allowedViews.add(UIScopedView.VIEW_NAME);
        allowedViews.add(ViewScopedView.VIEW_NAME);

        setMargin(true);
        setSpacing(true);
        addComponent(new Label("Here you can control the access to the different views within this particular UI. Uncheck a few boxes and try to navigate to their corresponding views. " +
                "In a real application, you would probably base the access decision on the current user's role or something similar."));

        addComponent(createViewCheckbox("Allow access to the Prototype Scoped View", PrototypeScopedView.VIEW_NAME));
        addComponent(createViewCheckbox("Allow access to the UI Scoped View", UIScopedView.VIEW_NAME));
        addComponent(createViewCheckbox("Allow access to the View Scoped View", ViewScopedView.VIEW_NAME));
    }

    private CheckBox createViewCheckbox(String caption, final String viewName) {
        final CheckBox checkBox = new CheckBox(caption, true);
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (checkBox.getValue()) {
                    allowedViews.add(viewName);
                } else {
                    allowedViews.remove(viewName);
                }
            }
        });
        return checkBox;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    @Override
    public boolean isAccessGranted(String beanName, UI ui) {
        final VaadinView annotation = applicationContext.findAnnotationOnBean(beanName, VaadinView.class);
        if (annotation != null) {
            return allowedViews.contains(annotation.name());
        } else {
            return false;
        }
    }

    @Override
    public boolean isAccessGranted(View view, UI ui) {
        // All the security checks are handled in the above method
        return true;
    }


}
