package org.vaadin.spring.boot.sample.security.security;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;

/**
 * @author petter@vaadin.com
 */
@VaadinView(name = HomeView.VIEW_NAME)
@UIScope
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class HomeView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "";

    @PostConstruct
    void init() {
        addComponent(new Label("This is the home view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}