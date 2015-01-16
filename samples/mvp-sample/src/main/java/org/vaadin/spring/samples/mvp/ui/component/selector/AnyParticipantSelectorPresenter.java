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
