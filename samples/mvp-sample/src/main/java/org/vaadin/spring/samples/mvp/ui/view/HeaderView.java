package org.vaadin.spring.samples.mvp.ui.view;

import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * Header >>> responsible for rendering fixed and dynamic elements.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@UIScope
@VaadinView(name = HeaderView.NAME)
public class HeaderView extends HorizontalLayout implements View {

    public static final String NAME = "header";

    @PostConstruct
    private void init() {
        setWidth("100%");
    }

    public void setContext(ControlsContext context) {
        if (getComponentCount() > 0) {
            removeAllComponents();
        }
        addComponent(buildControlsArea(context));
    }

    protected HorizontalLayout buildControlsArea(ControlsContext context) {
        HorizontalLayout left = new HorizontalLayout();
        left.setSpacing(true);
        left.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // ---- DYNAMIC HEADER ELEMENTS ----
        // Rendering depends on what tab was selected
        List<Component> controls = context.getControls();
        LayoutIntegrator.addComponents(left, controls.toArray(new Component[controls.size()]));

        return left;
    }


    @Override
    public void enter(ViewChangeEvent event) {

    }


}
