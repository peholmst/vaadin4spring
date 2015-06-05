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

import com.vaadin.spring.annotation.ViewScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Example of a view scoped bean that is not a UI component.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@ViewScope
@Component
public class ViewScopedBusinessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewScopedBusinessObject.class);

    @PostConstruct
    void init() {
        LOGGER.info("I'm being created: {}", this);
    }

    @PreDestroy
    void destroy() {
        // This method will get called when the user navigates away from the view. Try it out and check the logs.
        LOGGER.info("I'm being destroyed: {}", this);
    }

    public String sayHello() {
        return String.format("Hello, I'm %s and I'm view scoped! I will take care of you myself while this view is active. Try navigating away and back. You'll notice I've changed.", this);
    }
}
