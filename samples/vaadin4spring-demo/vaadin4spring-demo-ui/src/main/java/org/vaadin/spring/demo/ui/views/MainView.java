/*
 * Copyright 2015 Gert-Jan Timmer <gjr.timmer@gmail.com>.
 *
 */
package org.vaadin.spring.demo.ui.views;

import javax.annotation.PostConstruct;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.demo.ui.ApplicationUI;
import org.vaadin.spring.security.VaadinSecurity;

/**
 *
 * @author Gert-Jan Timmer <gjr.timmer@gmail.com>
 */
@VaadinView(name = MainView.NAME, ui = ApplicationUI.class)
@VaadinUIScope
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = -3780256410686877889L;

    public static final String NAME = "";

    @Autowired
    private VaadinSecurity security;

    @PostConstruct
    private void postConstruct() {

        setSizeFull();
        setSpacing(true);
        setMargin(true);

        addComponent(new Label("Main Application"));

        Button button = new Button("Click Me");
        button.addClickListener(e -> {
            this.addComponent(new Label("Thank you for clicking"));
        });
        addComponent(button);

    }

    @Override
    public void enter(ViewChangeEvent event) {}

}
