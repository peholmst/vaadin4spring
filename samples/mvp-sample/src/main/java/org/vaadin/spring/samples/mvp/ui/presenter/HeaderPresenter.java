package org.vaadin.spring.samples.mvp.ui.presenter;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
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
