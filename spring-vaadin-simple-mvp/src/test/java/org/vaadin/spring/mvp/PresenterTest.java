package org.vaadin.spring.mvp;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.mvp.explicit.ExplicitPresenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit Test for the {@link org.vaadin.spring.mvp.Presenter} class.
 *
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
public class PresenterTest {

    private ExplicitPresenter presenter;
    private FooView fooView;
    private EventBus eventBus;

    @Before
    public void setUp() {
        fooView = new FooView();
        eventBus = mock(EventBus.class);
        presenter = new ExplicitPresenter(fooView, eventBus);
    }

    @Test
    public void should_manage_underlying_view() {
        presenter.onNewCaption("test");
        assertEquals("test", fooView.getCaption());
    }
}
