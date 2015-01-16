package org.vaadin.spring.samples.mvp.ui.component.listener;

import java.util.Date;

import javax.inject.Inject;

import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusScope;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

@VaadinUIScope
@VaadinComponent
public class MarketDaySelectedListener implements ValueChangeListener {

    private static final long serialVersionUID = -123053805576375573L;

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
