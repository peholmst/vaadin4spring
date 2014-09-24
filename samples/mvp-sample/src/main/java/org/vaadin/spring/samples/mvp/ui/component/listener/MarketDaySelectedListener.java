package org.vaadin.spring.samples.mvp.ui.component.listener;

import java.util.Date;

import javax.inject.Inject;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

@UIScope
@VaadinComponent
public class MarketDaySelectedListener implements ValueChangeListener {

    @Inject
    @EventBusScope(EventScope.APPLICATION)
    private EventBus eventBus;

    @Override
    public void valueChange(ValueChangeEvent event) {
        // publish selected day as an ISO date String
        Date selectedDay = (Date) event.getProperty().getValue();
        String isoDay = SSTimeUtil.dateToIsoDay(selectedDay);
        eventBus.publish(this, isoDay);
    }

}
