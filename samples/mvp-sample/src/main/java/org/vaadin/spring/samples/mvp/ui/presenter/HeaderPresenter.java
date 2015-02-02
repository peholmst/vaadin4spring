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
package org.vaadin.spring.samples.mvp.ui.presenter;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.annotation.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;
import org.vaadin.spring.samples.mvp.ui.view.HeaderView;

@VaadinPresenter(viewName = HeaderView.NAME)
public class HeaderPresenter extends Presenter<HeaderView> {

    @EventBusListenerMethod(filter=StartupFilter.class)
    public void onStartup(Event<Action> event) {
        getEventBus().publish(this, ControlsContext.empty());
    }

    @EventBusListenerMethod
    public void onSetContext(ControlsContext context) {
        getView().setContext(context);
    }

}
