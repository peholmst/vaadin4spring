package org.vaadin.spring.samples.mvp.ui.presenter;

import javax.inject.Inject;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.service.UserService;
import org.vaadin.spring.samples.mvp.ui.view.BannerView;

@VaadinPresenter(viewName = BannerView.NAME)
public class BannerPresenter extends Presenter<BannerView> {

    @Inject
    UserService userService;

    @EventBusListenerMethod
    public void onStartup(Event<Action> event) {
        getView().setUser(userService.getUserName());
    }

}
