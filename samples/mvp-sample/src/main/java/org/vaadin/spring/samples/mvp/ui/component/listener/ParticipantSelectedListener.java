package org.vaadin.spring.samples.mvp.ui.component.listener;

import javax.inject.Inject;

import org.vaadin.spring.VaadinUIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

@VaadinUIScope
@VaadinComponent
public class ParticipantSelectedListener implements ValueChangeListener {

    @Inject
    @EventBusScope(EventScope.APPLICATION)
    private EventBus eventBus;

    @Override
    public void valueChange(ValueChangeEvent event) {
        // publish AssetOwnerDailyId
        eventBus.publish(this, event.getProperty().getValue());
    }

}
