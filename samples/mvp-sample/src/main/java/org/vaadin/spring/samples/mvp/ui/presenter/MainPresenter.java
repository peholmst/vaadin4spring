package org.vaadin.spring.samples.mvp.ui.presenter;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.view.BannerView;
import org.vaadin.spring.samples.mvp.ui.view.BodyView;
import org.vaadin.spring.samples.mvp.ui.view.FooterView;
import org.vaadin.spring.samples.mvp.ui.view.HeaderView;
import org.vaadin.spring.samples.mvp.ui.view.MainView;

import com.vaadin.ui.Component;

@VaadinPresenter(viewName = MainView.NAME)
public class MainPresenter extends Presenter<MainView> {

    @EventBusListenerMethod
    public void onStartup(Event<Action> event) {
        getView().setBanner((Component) getViewProvider().getView(BannerView.NAME));
        getView().setHeader((Component) getViewProvider().getView(HeaderView.NAME));
        getView().setBody((Component) getViewProvider().getView(BodyView.NAME));
        getView().setFooter((Component) getViewProvider().getView(FooterView.NAME));
    }

}
