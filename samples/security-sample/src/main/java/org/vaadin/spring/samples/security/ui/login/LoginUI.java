package org.vaadin.spring.samples.security.ui.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@VaadinUI(path = "/login")
@Title("Vaadin Spring-Security Sample")
@Theme("security")
@Widgetset("org.vaadin.spring.samples.security.Widgetset")
public class LoginUI extends UI {

    private static final long serialVersionUID = 5310014981075920878L;

    @Autowired
    private SpringViewProvider ViewProvider;
    
    @Override
    protected void init(VaadinRequest request) {
        
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(ViewProvider);
        setNavigator(navigator);
    }

}
