package org.vaadin.spring.samples.mvp.ui.presenter;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.view.BodyView;
import org.vaadin.spring.samples.mvp.ui.view.NavigationPanelView;
import org.vaadin.spring.samples.mvp.ui.view.TabPanelView;

import com.vaadin.ui.Component;

@VaadinPresenter(viewName = BodyView.NAME)
public class BodyPresenter extends Presenter<BodyView> {

    @EventBusListenerMethod
    public void onStartup(Event<Action> event) {
        getView().setNavigationPanel((Component) getViewProvider().getView(NavigationPanelView.NAME));
        getView().setTabbedPanel((Component) getViewProvider().getView(TabPanelView.NAME));
    }

}
