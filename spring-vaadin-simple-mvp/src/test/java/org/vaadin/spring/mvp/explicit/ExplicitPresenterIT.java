package org.vaadin.spring.mvp.explicit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.test.VaadinAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Integration Test that tests explicit wiring usage of the {@link org.vaadin.spring.mvp.Presenter} class.
 *
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ExplicitConfig.class)
@VaadinAppConfiguration
public class ExplicitPresenterIT {

    @Autowired
    private ExplicitPresenter explicitPresenter;

    @Autowired
    private EventBus eventBus;

    @Test
    public void should_listen_to_message() {
        String message = "message_from_explicit";
        eventBus.publish(this, message);
        assertEquals(message, explicitPresenter.getView().getCaption());
    }
}
