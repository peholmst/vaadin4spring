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
package org.vaadin.spring.samples.mvp.ui.component.listener;

import javax.inject.Inject;

import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusProxy;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElement;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;

@VaadinUIScope
@VaadinComponent
public class NavigationPanelListener implements ItemClickListener {

    private static final long serialVersionUID = -8657189602674122010L;

    @Inject
    private EventBus.UIEventBus eventBus;

    @Override
    public void itemClick(ItemClickEvent event) {
        eventBus.publish(this, ControlsContext.empty());
        // Pick only left mouse clicks
        if (event.getButton() == MouseButton.LEFT) {
            NavElement ne = (NavElement) event.getItemId();
            eventBus.publish(this, ne);
        }
    }

}
