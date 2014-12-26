package org.vaadin.spring.boot.sample.security.security;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.security.Security;

import javax.annotation.PostConstruct;

/**
 * @author petter@vaadin.com
 */
@UIScope
@VaadinComponent
public class SecuredUIContent extends VerticalLayout {

    @Autowired
    SpringViewProvider viewProvider;
    @Autowired
    Security security;
    @Autowired
    Authentication currentUser;
    @Autowired
    ErrorView errorView;
    private Panel viewContainer;

    @PostConstruct
    void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final HorizontalLayout toolbar = new HorizontalLayout(
                new Button("Go to the Home view", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        getUI().getNavigator().navigateTo(HomeView.VIEW_NAME);
                    }
                }),
                new Button("Go to User view", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        getUI().getNavigator().navigateTo(UserView.VIEW_NAME);
                    }
                }),
                new Button("Go to Admin view", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        getUI().getNavigator().navigateTo(AdminView.VIEW_NAME);
                    }
                }),
                new Button("Logout", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        security.logout();
                        getUI().getPage().reload();
                    }
                })
        );
        toolbar.setSpacing(true);
        addComponent(toolbar);
        viewContainer = new Panel();
        viewContainer.setSizeFull();
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1f);
    }

    @Override
    public void attach() {
        final Navigator navigator = new Navigator(getUI(), viewContainer);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(errorView);
    }
}
