package org.vaadin.spring.mvp.explicit;

import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.mvp.FooView;
import org.vaadin.spring.mvp.Presenter;

public class ExplicitPresenter extends Presenter<FooView> {

    public ExplicitPresenter(FooView view, EventBus eventBus) {
        super(view, eventBus);
    }

    @EventBusListenerMethod
    public void onNewCaption(String caption) {
        getView().setCaption(caption);
    }
}
