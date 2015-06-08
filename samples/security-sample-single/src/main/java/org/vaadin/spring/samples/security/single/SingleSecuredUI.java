package org.vaadin.spring.samples.security.single;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.samples.security.single.events.LoginEvent;
import org.vaadin.spring.samples.security.single.events.LogoutEvent;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * Created by petterwork on 05/06/15.
 */
@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Push(transport = Transport.LONG_POLLING) // TODO Should also support web sockets
public class SingleSecuredUI extends UI {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    VaadinSecurity vaadinSecurity;

    @Autowired
    EventBus.SessionEventBus eventBus;

    @Override
    protected void init(VaadinRequest request) {
        if (vaadinSecurity.isAuthenticated()) {
            showMainScreen();
        } else {
            showLoginScreen();
        }
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.subscribe(this);
    }

    @Override
    public void detach() {
        eventBus.unsubscribe(this);
        super.detach();
    }

    private void showLoginScreen() {
        setContent(applicationContext.getBean(LoginScreen.class));
    }

    private void showMainScreen() {
        setContent(applicationContext.getBean(MainScreen.class));
    }

    @EventBusListenerMethod
    void onLogin(LoginEvent loginEvent) {
        access(new Runnable() {
            @Override
            public void run() {
                showMainScreen();
            }
        });
    }

    @EventBusListenerMethod
    void onLogout(LogoutEvent logoutEvent) {
        access(new Runnable() {
            @Override
            public void run() {
                showLoginScreen();
            }
        });
    }

}
