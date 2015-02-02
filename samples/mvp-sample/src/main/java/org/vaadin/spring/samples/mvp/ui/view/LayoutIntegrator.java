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
package org.vaadin.spring.samples.mvp.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

/**
 * A collection of utility methods for integrating (a) component(s) into a layout.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
class LayoutIntegrator {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(LayoutIntegrator.class);

    static void addComponents(Layout layout, Component[] components) {
        layout.addComponents(components);
    }

}
