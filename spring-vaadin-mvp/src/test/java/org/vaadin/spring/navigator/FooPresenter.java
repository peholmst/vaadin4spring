package org.vaadin.spring.navigator;

import org.vaadin.spring.VaadinUIScope;
import org.vaadin.spring.events.EventBusListenerMethod;

@VaadinUIScope
@VaadinPresenter(viewName=FooView.NAME)
class FooPresenter extends Presenter<FooView> {

    @EventBusListenerMethod
    public void onSetFooOnFooView(String someFoo) {
        getView().setFoo(someFoo);
    }
}
