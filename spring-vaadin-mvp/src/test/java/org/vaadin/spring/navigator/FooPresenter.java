package org.vaadin.spring.navigator;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

@VaadinUIScope
@VaadinPresenter(viewName=FooView.NAME)
class FooPresenter extends Presenter<FooView> {

    @EventBusListenerMethod
    public void onSetFooOnFooView(String someFoo) {
        getView().setFoo(someFoo);
    }
}
