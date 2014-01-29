package org.vaadin.spring.boot.sample.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.spring.VaadinUI;

/**
 * Vaadin UI mapped to the root path of the servlet. This UI also uses a custom theme.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUI
@Theme("sample")
public class RootUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Root UI");
        setContent(new Label("Hello! I'm the root UI!"));
    }
}
