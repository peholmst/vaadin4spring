/*
 * Copyright 2015 Gert-Jan Timmer <gjr.timmer@gmail.com>.
 *
 */
package org.vaadin.spring.demo.ui;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUI;
import org.vaadin.spring.demo.Constants;
import org.vaadin.spring.navigator.SpringViewProvider;

@VaadinUI
@Title(Constants.Application.TITLE)
@Theme(Constants.Application.THEME)
@Widgetset(Constants.Application.WIDGETSET)
public class ApplicationUI extends UI {

    private static final long serialVersionUID = -5493486283174542133L;

    @Autowired
    private SpringViewProvider ViewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(ViewProvider);
        setNavigator(navigator);

    }
}
