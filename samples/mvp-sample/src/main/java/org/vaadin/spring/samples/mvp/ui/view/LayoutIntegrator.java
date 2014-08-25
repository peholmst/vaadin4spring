package org.vaadin.spring.samples.mvp.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

/**
 * A collection of utility methods for integrating (a) component(s) into a layout.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
class LayoutIntegrator {

    private static Logger log = LoggerFactory.getLogger(LayoutIntegrator.class);

    static void addComponents(Layout layout, Component[] components) {
        layout.addComponents(components);
    }

}
