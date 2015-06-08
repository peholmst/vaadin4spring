package org.vaadin.spring.samples.security.single;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CustomComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

/**
 * TODO Document me!
 */
@UIScope
@SpringComponent
public class MainScreen extends CustomComponent {

    private final VaadinSecurity vaadinSecurity;
    private final SpringViewProvider springViewProvider;

    @Autowired
    public MainScreen(VaadinSecurity vaadinSecurity, SpringViewProvider springViewProvider) {
        this.vaadinSecurity = vaadinSecurity;
        this.springViewProvider = springViewProvider;
    }

}
