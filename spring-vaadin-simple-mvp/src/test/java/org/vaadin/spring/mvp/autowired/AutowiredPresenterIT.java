package org.vaadin.spring.mvp.autowired;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Integration Test that tests auto-wiring usage of the {@link org.vaadin.spring.mvp.Presenter} class.
 *
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ScanConfig.class)
@VaadinAppConfiguration
public class AutowiredPresenterIT {

    @Autowired
    private AutowiredPresenter autowiredPresenter;

    @Autowired
    private EventBus eventBus;

    @Test
    public void should_listen_to_message() {
        String message = "message_from_autowired";
        eventBus.publish(this, message);
        assertEquals(message, autowiredPresenter.getView().getCaption());
    }
}
