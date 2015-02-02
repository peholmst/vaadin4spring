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
package org.vaadin.spring.samples.mvp.ui.component.selector;

import org.joda.time.DateTime;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.annotation.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.component.listener.MarketDaySelectedListener;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

@VaadinPresenter(viewName = AnyParticipantSelector.NAME)
public class AnyParticipantSelectorPresenter extends Presenter<ParticipantSelector> {

    @EventBusListenerMethod
    public void onEvent(Event<Object> event) {
        Object source = event.getSource();
        if (source instanceof MarketDaySelectedListener) {
            String payload = (String) event.getPayload();
            DateTime effectiveDay = SSTimeUtil.isoDayToDateTime(payload);
            getView().setEffectiveDay(effectiveDay);
        }
    }

}
