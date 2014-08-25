package org.vaadin.spring.samples.mvp.ui.component;

import com.vaadin.ui.NativeButton;

public class ControlButton extends NativeButton {

    private static final long serialVersionUID = 1L;

    public ControlButton(String label, ClickListener listener) {
        this.addStyleName("a-controlButton");
        this.setCaption(label);
        this.addClickListener(listener);
    }
}
