package org.vaadin.spring.navigator;

import org.vaadin.spring.VaadinUIScope;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Panel;

@VaadinUIScope
@VaadinView(name = FooView.NAME)
class FooView extends Panel implements View {

    public static final String NAME = "foo";

    private String foo;

    /**
     * Sets this Panel's caption
     * (and also sets state of foo variable)
     * @param someFoo a caption
     */
    public void setFoo(String foo) {
        this.foo = foo;
        setCaption(foo);
    }

    // normally one wouldn't provide an accessor like this
    // this is here to verify state was properly set
    public String getFoo() {
        return foo;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
