package org.vaadin.spring.navigator;

import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

/**
 * Base class for presentation.
 * Derivatives must be annotated with {@link VaadinPresenter}.
 * Works with {@link EventBus} to subscribe to events and set model data
 * on an implementor of {@link com.vaadin.navigator.View}.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 * @param <V> an implementation of {@link com.vaadin.navigator.View}
 */
public abstract class Presenter<V extends View> {

    private static Logger logger = LoggerFactory.getLogger(Presenter.class);

    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    private EventBus eventBus;

    @PostConstruct
    protected void init() {
        eventBus.subscribe(this);
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    /**
     * Hands back the View that this Presenter works with
     * A match is made if the ViewProvider finds a VaadinView annotated View whose name matches Presenter's viewName
     * @return an implementor of {@link View}
     */
    public V getView() {
        V result = null;
        Annotation[] annotations = getClass().getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            int i = 0;
            for (Annotation a: annotations) {
                if (a.annotationType().equals(VaadinPresenter.class)) {
                    VaadinPresenter vp = (VaadinPresenter) a;
                    result = (V) viewProvider.getView(vp.viewName());
                    i++;
                    break;
                }
            }
            if (i == 0) {
                logger.error("Presenter does not have a @VaadinPresenter annotation!");
            }
        } else {
            logger.error("Presenter does not have any annotations!");
        }
        return result;
    }


    /**
     * Allows this Presenter to (possibly) work with other views
     * @return a ViewProvider
     */
    public ViewProvider getViewProvider() {
        return this.viewProvider;
    }

    @PreDestroy
    // It's good manners to do this, even though we should be automatically unsubscribed
    // when the UI is garbage collected
    void destroy() {
        getEventBus().unsubscribe(this);
    }

}
