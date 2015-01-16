package org.vaadin.spring.samples.mvp.ui.presenter;

import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.navigator.Presenter;
import org.vaadin.spring.navigator.annotation.VaadinPresenter;
import org.vaadin.spring.samples.mvp.ui.component.nav.NavElement;
import org.vaadin.spring.samples.mvp.ui.view.TabPanelView;

/**
 * Presenter responsible for populating tabs (i.e., just the captions) in the {@link TabPanelView}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@VaadinPresenter(viewName = TabPanelView.NAME)
public class TabPanelPresenter extends Presenter<TabPanelView> {

    @EventBusListenerMethod
    public void onPopulateTabCaptions(NavElement element) {
        getView().setOrigin(element);
    }



}
