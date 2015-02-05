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

import org.springframework.stereotype.Component;
import org.vaadin.spring.navigator.annotation.VaadinViewScope;

import java.io.Serializable;

/**
 * Example of a non-UI view scoped component.
 * It is used to demonstrate the behavior of the view scope.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinViewScope
@Component
public class ViewScopedObject implements Serializable {

    public String foo() {
        return "Hello, I'm " + this;
    }
}
