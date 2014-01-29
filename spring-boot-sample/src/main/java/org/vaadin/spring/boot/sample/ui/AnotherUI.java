package org.vaadin.spring.boot.sample.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.spring.VaadinUI;

/**
 * Vaadin UI mapped to the {@code "/anotherUI"} path of the servlet.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@VaadinUI(path = "/anotherUI")
public class AnotherUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Another UI");
        setContent(new Label("Hello! I'm a different UI mapped at a different URL!"));
    }
}
