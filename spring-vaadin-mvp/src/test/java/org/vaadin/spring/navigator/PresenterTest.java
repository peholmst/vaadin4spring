package org.vaadin.spring.navigator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.test.VaadinAppConfiguration;


/**
 * Basic test for proper functioning of an implementor of {@link Presenter}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@VaadinAppConfiguration
public class PresenterTest {

    @Autowired
    private FooPresenter presenter;

    @Autowired
    private SpringViewProvider provider;

    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    private EventBus eventBus;

    @Test
    public void testValidPresenter() {
        Assert.assertNotNull(presenter);
        Assert.assertNotNull(presenter.getEventBus());
        Assert.assertTrue(presenter.getView().equals(provider.getView(FooView.NAME)));

        String message = "Some foo for you";
        eventBus.publish(this, message);

        Assert.assertEquals(message, presenter.getView().getFoo());
    }

}
