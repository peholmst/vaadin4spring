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

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.navigator.annotation.VaadinViewScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Example of a view scoped UI component that can be injected into a view.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinViewScope
@VaadinComponent
public class ViewScopedComponent extends Label {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewScopedComponent.class);

    @Autowired
    ViewScopedBusinessObject viewScopedBusinessObject;

    @PostConstruct
    void init() {
        LOGGER.info("I'm being created: {}", this);
        setValue("I'm a view scoped component. This is the result from invoking the view scoped business object: <b>" + viewScopedBusinessObject.sayHello() + "</b>. " +
                "Try invoking the same business object by clicking the button below. You'll see the output is the same, which means the same instance has been injected into both this component and the view.");
        setStyleName(ValoTheme.LABEL_COLORED);
        setContentMode(ContentMode.HTML);
    }

    @PreDestroy
    void destroy() {
        // This method will get called when the user navigates away from the view. Try it out and check the logs.
        LOGGER.info("I'm being destroyed: {}", this);
    }

}
