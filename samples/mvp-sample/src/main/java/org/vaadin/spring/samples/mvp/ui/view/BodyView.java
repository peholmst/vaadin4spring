package org.vaadin.spring.samples.mvp.ui.view;

import javax.annotation.PostConstruct;

import org.vaadin.spring.VaadinUIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.ui.component.layout.Styles;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;

@VaadinUIScope
@VaadinView(name = BodyView.NAME)
public class BodyView extends HorizontalSplitPanel implements View {

    public static final String NAME = "body";


    @PostConstruct
    private void init() {
        setSplitPosition(20f, Unit.PERCENTAGE);
        setStyleName(Styles.SPLITPANEL_SMALL);
    }

    public void setNavigationPanel(Component navigationPanel) {
        setFirstComponent(navigationPanel);
    }

    public void setTabbedPanel(Component tabPanel) {
        setSecondComponent(tabPanel);
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }
}
