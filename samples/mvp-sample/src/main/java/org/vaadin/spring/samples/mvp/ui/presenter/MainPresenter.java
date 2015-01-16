package org.vaadin.spring.samples.mvp.ui.presenter;

import javax.inject.Inject;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.annotation.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.view.FooterView;
import org.vaadin.spring.samples.mvp.ui.view.MainView;

import com.vaadin.ui.Component;

@VaadinPresenter(viewName = MainView.NAME)
public class MainPresenter extends Presenter<MainView> {

    @Inject
    BannerPresenter banner;

    @Inject
    HeaderPresenter header;

    @Inject
    BodyPresenter body;

    @EventBusListenerMethod(scope=EventScope.SESSION, filter=StartupFilter.class)
    public void onStartup(Event<Action> event) {
        getView().setBanner(banner.getView());
        getView().setHeader(header.getView());
        getView().setBody(body.getView());
        getView().setFooter((Component) getViewProvider().getView(FooterView.NAME));
    }

}
