package org.vaadin.spring.samples.mvp.ui.presenter;


import org.apache.commons.collections4.CollectionUtils;
import org.vaadin.spring.samples.mvp.ui.component.util.ControlsContext;



import com.vaadin.ui.Component;

public class Screen {

    private static final Double DEFAULT_VERSION = 1.0;
    private static final Action DEFAULT_ACTION = Action.START;

    private final String viewName;
    private final Double version;
    private final ControlsContext context;
    private final Action action;

    public Screen(String viewName) {
        this(viewName, DEFAULT_VERSION, ControlsContext.empty(), DEFAULT_ACTION);
    }

    public Screen(String viewName, ControlsContext context, Action action) {
        this(viewName, DEFAULT_VERSION, context, action);
    }

    public Screen(String viewName, Double version) {
        this(viewName, version, ControlsContext.empty(), DEFAULT_ACTION);
    }

    public Screen(String viewName, Double version, ControlsContext context) {
        this(viewName, version, context, DEFAULT_ACTION);
    }

    public Screen(String viewName, Double version, ControlsContext context, Action action) {
        this.viewName = viewName;
        this.version = version;
        this.context = context;
        this.action = action;
    }

    public String getViewName() {
        return viewName;
    }

    public Double getVersion() {
        return version;
    }

    public ControlsContext getContext() {
        return context;
    }

    public Component getControl(Class<? extends Component> type) {
        Component component = null;
        if (hasControls()) {
            for (Component c: getContext().getControls()) {
                if (c.getClass().equals(type)) {
                    component = c;
                    break;
                }
            }
        }
        return component;
    }

    public Action getAction() {
        return action;
    }

    public boolean hasControls() {
        boolean result = false;
        ControlsContext context = getContext();
        if (context != null) {
            if (CollectionUtils.isNotEmpty(context.getControls())) {
                result = true;
            }
        }
        return result;
    }

}
