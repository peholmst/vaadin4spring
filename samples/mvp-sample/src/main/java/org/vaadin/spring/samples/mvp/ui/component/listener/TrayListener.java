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