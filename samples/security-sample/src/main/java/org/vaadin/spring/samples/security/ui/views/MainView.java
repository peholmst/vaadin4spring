package org.vaadin.spring.samples.security.ui.views;

import javax.annotation.PostConstruct;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@VaadinView(name = MainView.NAME)
@VaadinUIScope
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = -3780256410686877889L;
    
    public static final String NAME = "";
    
    @PostConstruct
    private void postConstruct() {
        
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        
        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 8086462724732012808L;

            @Override
            public void buttonClick(ClickEvent event) {
                addComponent(new Label("Thank you for clicking"));
            }
        });
        addComponent(button);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
