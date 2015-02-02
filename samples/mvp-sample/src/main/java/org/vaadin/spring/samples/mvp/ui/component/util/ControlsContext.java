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
package org.vaadin.spring.samples.mvp.ui.component.util;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;

/**
 * <p>User Interface Controls Context</p>
 * <p>It's assumed that a screen will be filtered by one or more controls.
 * Context is usually nothing more than a bag of "hints" that inform a screen which controls to render.</p>
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class ControlsContext {

    private final List<Component> controls = new ArrayList<>();

    public List<Component> getControls() {
        return controls;
    }

    public static ControlsContext empty() {
        return new ControlsContext();
    }

}
