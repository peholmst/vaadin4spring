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

import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.annotation.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElement;
import org.vaadin.spring.samples.mvp.ui.view.TabPanelView;

/**
 * Presenter responsible for populating tabs (i.e., just the captions) in the {@link TabPanelView}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@VaadinPresenter(viewName = TabPanelView.NAME)
public class TabPanelPresenter extends Presenter<TabPanelView> {

    @EventBusListenerMethod
    public void onPopulateTabCaptions(NavElement element) {
        getView().setOrigin(element);
    }



}
