package org.vaadin.spring.mvp.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.mvp.FooView;
import org.vaadin.spring.mvp.Presenter;

@VaadinUIScope
@VaadinComponent
public class AutowiredPresenter extends Presenter<FooView> {

    @Autowired
    public AutowiredPresenter(FooView view, EventBus eventBus) {
        super(view, eventBus);
    }

    @EventBusListenerMethod
    public void onNewCaption(String caption) {
        getView().setCaption(caption);
    }
}
