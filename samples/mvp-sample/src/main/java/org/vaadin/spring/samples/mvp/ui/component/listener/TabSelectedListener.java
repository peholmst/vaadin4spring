package org.vaadin.spring.samples.mvp.ui.component.listener;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;
import org.vaadin.spring.samples.mvp.ui.presenter.Screen;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

@VaadinComponent
public class TabSelectedListener implements SelectedTabChangeListener {

    @Inject
    @EventBusScope(EventScope.APPLICATION)
    private EventBus eventBus;

    @Override
    public void selectedTabChange(final SelectedTabChangeEvent event) {
        eventBus.publish(this, ControlsContext.empty());
        Component c = event.getTabSheet().getSelectedTab();
        if (View.class.isAssignableFrom(c.getClass())) {
            View v = (View) c;
            Annotation[] annotations = v.getClass().getAnnotations();
            if (ArrayUtils.isNotEmpty(annotations)) {
                for (Annotation a: annotations) {
                    if (a instanceof VaadinView) {
                        // TODO make a request to enhance VaadinView annotation with an additional version attribute
                        VaadinView vv = (VaadinView) a;
                        eventBus.publish(this, new Screen(vv.name()));
                        break;
                    }
                }
            }
        }
    }

}
