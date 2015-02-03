package org.vaadin.spring.navigator.internal;

import com.vaadin.navigator.View;
import org.vaadin.spring.internal.BeanStore;

import java.io.Serializable;

/**
 * Created by petterwork on 02/02/15.
 */
public class CurrentView implements Serializable {

    private final BeanStore beanStore;

    private final String viewName;

    private View viewComponent;

    public CurrentView(String viewName) {
        beanStore = new BeanStore("View:" + viewName);
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public BeanStore getBeanStore() {
        return beanStore;
    }

    public View getViewComponent() {
        return viewComponent;
    }

    public void setViewComponent(View viewComponent) {
        this.viewComponent = viewComponent;
    }
}
