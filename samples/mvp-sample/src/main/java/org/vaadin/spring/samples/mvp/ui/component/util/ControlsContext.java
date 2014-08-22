package org.vaadin.spring.samples.mvp.ui.component.util;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;

/**
 * <p>User Interface Controls Context</p>
 * <p>It's assumed that a screen will be filtered by one or more controls.
 * Context is usually nothing more than a bag of "hints" that inform a screen which controls to render.</p>
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class ControlsContext {

    private final List<Component> controls = new ArrayList<>();

    public List<Component> getControls() {
        return controls;
    }

    public static ControlsContext empty() {
        return new ControlsContext();
    }

}
