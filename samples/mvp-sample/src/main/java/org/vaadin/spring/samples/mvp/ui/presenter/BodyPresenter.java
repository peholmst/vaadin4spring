package org.vaadin.spring.samples.mvp.ui.presenter;

import javax.inject.Inject;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.view.BodyView;

@VaadinPresenter(viewName = BodyView.NAME)
public class BodyPresenter extends Presenter<BodyView> {

    @Inject
    NavigationPanelPresenter nav;

    @Inject
    TabPanelPresenter tab;

    @EventBusListenerMethod(scope=EventScope.SESSION, filter=StartupFilter.class)
    public void onStartup(Event<Action> event) {
        getView().setNavigationPanel(nav.getView());
        getView().setTabbedPanel(tab.getView());
    }

}
