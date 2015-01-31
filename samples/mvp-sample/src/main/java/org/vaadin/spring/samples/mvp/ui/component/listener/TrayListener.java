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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Listens for specific events and consistently displays notifications
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class TrayListener implements ValueChangeListener, ClickListener {

    private static final long serialVersionUID = 1L;

    private String message;

    public TrayListener(String message) {
        this.message = message;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        String valueString = String.valueOf(event.getProperty().getValue());
        Notification.show(message, valueString, Type.TRAY_NOTIFICATION);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Notification.show(message, Type.TRAY_NOTIFICATION);
    }

}