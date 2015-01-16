package org.vaadin.spring.samples.mvp.ui.view;

import javax.annotation.PostConstruct;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinUIScope
@VaadinView(name = MainView.NAME)
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = 6041151920926888211L;
    public static final String NAME = "main";


    @PostConstruct
    private void init() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }


    public void setBanner(Component banner) {
        addComponent(banner);
    }

    public void setHeader(Component header) {
        addComponent(header);
        setExpandRatio(header, 1);
    }

    public void setBody(Component body) {
        addComponent(body);
        setExpandRatio(body, 10);
    }

    public void setFooter(Component footer) {
        addComponent(footer);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
