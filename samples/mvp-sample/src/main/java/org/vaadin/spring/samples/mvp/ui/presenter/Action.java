package org.vaadin.spring.samples.mvp.ui.presenter;


/**
 * A token used for event payloads
 * A {@link org.vaadin.spring.navigator.Presenter} will typically listen for such an event.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public enum Action {
    START,
    GET_DATA
}
