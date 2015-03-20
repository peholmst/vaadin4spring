package org.vaadin.spring.samples.portlet.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.spring.annotation.VaadinPortletUI;

@VaadinPortletUI
public class MyVaadinPortletUI extends UI {
    private static final long serialVersionUID = 1283967943845134541L;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hello! This is a simple portlet with Spring support!"));
    }
}
