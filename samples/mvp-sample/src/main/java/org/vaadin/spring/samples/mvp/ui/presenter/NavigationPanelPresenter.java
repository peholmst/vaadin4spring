package org.vaadin.spring.samples.mvp.ui.presenter;

import java.util.List;

import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElement;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElementFactory;
import org.vaadin.spring.samples.mvp.ui.view.NavigationPanelView;

/**
 * Presenter responsible for setting model data on the {@link NavigationPanelView}.
 * It employs a factory to construct model data in the form of <code>List&lt;NavElement&gt;</code>.
 * Model data is retrieved from <code>navElements.json</code>.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@VaadinPresenter(viewName = NavigationPanelView.NAME)
public class NavigationPanelPresenter extends Presenter<NavigationPanelView> {

    private static final String NAV_ELEMENTS_FILE = "navElements.json";

    @EventBusListenerMethod(filter=StartupFilter.class)
    public void onStartup(Event<Action> event) {
        NavElementFactory factory = new NavElementFactory();
        getEventBus().publish(this, factory.getNavElements(NAV_ELEMENTS_FILE));
    }

    @EventBusListenerMethod
    public void onSetData(List<NavElement> navElements) {
        getView().setData(navElements);
    }

}
