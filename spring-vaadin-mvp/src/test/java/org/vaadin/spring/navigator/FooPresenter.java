package org.vaadin.spring.navigator;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBusListenerMethod;

@UIScope
@VaadinPresenter(viewName=FooView.NAME)
class FooPresenter extends Presenter<FooView> {

    @EventBusListenerMethod
    public void onSetFooOnFooView(String someFoo) {
        getView().setFoo(someFoo);
    }
}
