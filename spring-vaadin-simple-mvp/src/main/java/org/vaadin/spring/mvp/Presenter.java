package org.vaadin.spring.mvp;

import org.springframework.util.Assert;
import org.vaadin.spring.events.EventBus;

/**
 * Base class for a <em>Presenter</em> implementation.
 *
 * @param <T> <em>View</em> type
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
public abstract class Presenter<T> {

    private final T view;
    private final EventBus eventBus;

    /**
     * The constructor automatically subscribes to event bus events.
     *
     * @param view <em>View</em>
     * @param eventBus Vaadin even bus
     */
    public Presenter(T view, EventBus eventBus) {
        Assert.notNull(view);
        Assert.notNull(eventBus);
        this.view = view;
        this.eventBus = eventBus;
        eventBus.subscribe(this);
    }

    public T getView() {
        return view;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
