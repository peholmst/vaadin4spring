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

import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.navigator.annotation.VaadinViewScope;

import javax.annotation.PostConstruct;

/**
 * Example of a UI view scoped component that uses another view scoped component.
 * It is used to demonstrate the behavior of the view scope.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinComponent
@VaadinViewScope
public class ViewScopedComponent extends Label {

    @Autowired
    ViewScopedObject viewScopedObject;

    @PostConstruct
    void init() {
        setValue(viewScopedObject.foo());
    }
}
