package org.vaadin.spring.samples.mvp.ui.component.listener;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;
import org.vaadin.spring.samples.mvp.ui.presenter.Screen;
import org.vaadin.spring.samples.mvp.ui.view.HeaderView;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

@UIScope
@VaadinComponent
public class TabSelectedListener implements SelectedTabChangeListener {

    private static final long serialVersionUID = -6279286514453567595L;

    @Inject
    ApplicationContext context;

    @Inject
    private EventBus eventBus;

    @Override
    // this is kind of hacky!
    // since all presenters are UI-scoped we need to call them into being before publishing events
    // that can be received and handled by them
    public void selectedTabChange(final SelectedTabChangeEvent event) {
        eventBus.publish(context.getBean(HeaderView.class), ControlsContext.empty());
        Component c = event.getTabSheet().getSelectedTab();
        if (View.class.isAssignableFrom(c.getClass())) {
            View v = (View) c;
            Annotation[] annotations = v.getClass().getAnnotations();
            if (ArrayUtils.isNotEmpty(annotations)) {
                for (Annotation a: annotations) {
                    if (a instanceof VaadinView) {
                        VaadinView vv = (VaadinView) a;
                        // really just need the presenter whose name matched VaadinView#name to be
                        // called into being, but Spring caches scoped-beans too
                        context.getBeansWithAnnotation(VaadinPresenter.class);
                        eventBus.publish(this, new Screen(vv.name()));
                        break;
                    }
                }
            }
        }
    }

}
