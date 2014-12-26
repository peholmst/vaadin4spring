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
@VaadinView(name = UserView.VIEW_NAME)
@UIScope
@Secured(Roles.USER)
public class UserView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "userView";

    @PostConstruct
    void init() {
        addComponent(new Label("This is the user view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}