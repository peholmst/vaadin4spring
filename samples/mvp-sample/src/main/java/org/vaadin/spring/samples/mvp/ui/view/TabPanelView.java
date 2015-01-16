package org.vaadin.spring.samples.mvp.ui.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.spring.samples.mvp.ui.component.grid.DataGrid;
import org.vaadin.spring.samples.mvp.ui.component.listener.TabSelectedListener;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElement;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElementFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

@VaadinUIScope
@VaadinView(name = TabPanelView.NAME)
public class TabPanelView extends TabSheet implements View {

    private static final long serialVersionUID = -4866260647690189827L;

    private static Logger log = LoggerFactory.getLogger(TabPanelView.class);

    public static final String NAME = "tabPanel";

    @Inject
    private SpringViewProvider viewProvider;

    @Inject
    private TabSelectedListener selectedListener;

    @PostConstruct
    private void init() {
        setVisible(false);
        setSizeFull();
        addSelectedTabChangeListener(selectedListener);
    }

    public void setOrigin(NavElement origin) {
        String tabCollectionId = String.valueOf(origin.getId().intValue());
        addCaptions(origin, getNavElements(tabCollectionId));
    }

    protected List<NavElement> getNavElements(String id) {
        NavElementFactory factory = new NavElementFactory();
        return factory.getNavElements("screens/" + id + "/tabs.json");
    }

    protected void addCaptions(NavElement origin, List<NavElement> targets) {
        if (getComponentCount() > 0) {
            removeAllComponents();
        }
        if (CollectionUtils.isNotEmpty(targets)) {
            setVisible(true);
            Component c;
            String viewName;
            for (NavElement target: targets) {
                viewName = getViewName(origin, target);
                c = generateTab(viewName);
                if (c != null) {
                    addTab(c, target.getName());
                } else {
                    log.warn("View [{}] not available (or not yet implemented).", viewName);
                    addTab(DataGrid.emptyGrid(), target.getName());
                }
            }
        } else {
            setVisible(false);
        }
    }

    private Component generateTab(String viewName) {
        return (Component) viewProvider.getView(viewName);
    }

    private String getViewName(NavElement origin, NavElement target) {
        StringBuilder builder = new StringBuilder();
        builder.append(origin.getAlias());
        builder.append("/");
        builder.append(target.getAlias());
        return builder.toString();
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
