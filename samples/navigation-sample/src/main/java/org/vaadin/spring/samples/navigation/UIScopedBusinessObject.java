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

import com.vaadin.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Example of a UI scoped bean that is not a UI component.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@UIScope
@Component
public class UIScopedBusinessObject implements Serializable {

    public String sayHello() {
        return String.format("Hello, I'm %s and I'm UI scoped! I will take care of you myself during the lifecycle of the UI.", this);
    }
}
