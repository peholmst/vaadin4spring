package org.vaadin.spring.samples.mvp.ui.view;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomLayout;

@VaadinUIScope
@VaadinView(name = FooterView.NAME)
public class FooterView extends CustomLayout implements View {

    private static final long serialVersionUID = 1L;
    public static final String NAME = "footer";

    public FooterView() {
        this.setHeight("20px");
    }


    @Override
    public void enter(ViewChangeEvent event) {

    }


}
