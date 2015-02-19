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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

@VaadinUIScope
@VaadinComponent
public class ParticipantSelectedListener implements ValueChangeListener {

    private static final long serialVersionUID = 331391425040065709L;

    @Inject
    private EventBus.UIEventBus eventBus;

    @Override
    public void valueChange(ValueChangeEvent event) {
        // publish AssetOwnerDailyId
        eventBus.publish(this, event.getProperty().getValue());
    }

}
