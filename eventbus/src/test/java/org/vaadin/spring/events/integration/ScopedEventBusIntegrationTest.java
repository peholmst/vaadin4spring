package org.vaadin.spring.events.integration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.SpringVaadinSession;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.config.EventBusConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Test cases for the different event bus scopes
 *
 * @author erik@vaadin.com
 * @since 14/02/2019
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EventBusConfiguration.class, ScopedEventBusIntegrationTest.Config.class})
class ScopedEventBusIntegrationTest {

    @Configuration
    @EnableVaadin
    public static class Config {
    }

    public static class TestSession extends SpringVaadinSession {
        TestSession() {
            super(Mockito.mock(VaadinService.class));
        }

        @Override
        public boolean hasLock() {
            return true;
        }

        @Override
        public void lock() {
        }

        @Override
        public void unlock() {
        }
    }

    private static int uiId = 0;

    @Autowired
    ApplicationContext applicationContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        VaadinSession.setCurrent(new TestSession());
        UI.setCurrent(createMockUI());
    }

    @Test
    void testSameUIReturnsSameUIEventBus() {
        EventBus.UIEventBus uiBus = applicationContext.getBean(EventBus.UIEventBus.class);
        EventBus.UIEventBus uiBus2 = applicationContext.getBean(EventBus.UIEventBus.class);
        assertEquals(uiBus, uiBus2, "Same UI should return same UIEventBus");
    }

    @Test
    void testDifferentUIReturnsDifferentUIEventBus() {
        EventBus.UIEventBus uiBus = applicationContext.getBean(EventBus.UIEventBus.class);
        UI.setCurrent(createMockUI());
        EventBus.UIEventBus uiBus2 = applicationContext.getBean(EventBus.UIEventBus.class);
        assertNotEquals(uiBus, uiBus2, "Different UIs should return different UIEventBuses");
    }

    @Test
    void testNoUIThrowsBeanCreationException() {
        UI.setCurrent(null);
        assertThrows(BeanCreationException.class, () -> applicationContext.getBean(EventBus.UIEventBus.class));
    }

    @Test
    void testSameSessionReturnsSameSessionEventBus() {
        EventBus.SessionEventBus sessionBus = applicationContext.getBean(EventBus.SessionEventBus.class);
        EventBus.SessionEventBus sessionBus2 = applicationContext.getBean(EventBus.SessionEventBus.class);
        assertEquals(sessionBus, sessionBus2, "Same session should return same SessionEventBus");
    }

    @Test
    void testSameSessionDifferentUIReturnsSameSessionEventBus() {
        EventBus.SessionEventBus sessionBus = applicationContext.getBean(EventBus.SessionEventBus.class);
        UI.setCurrent(createMockUI());
        EventBus.SessionEventBus sessionBus2 = applicationContext.getBean(EventBus.SessionEventBus.class);
        assertEquals(sessionBus, sessionBus2, "Same session different UIs should return same SessionEventBus");
    }

    @Test
    void testDifferentSessionsReturnDifferentSessionEventBuses() {
        EventBus.SessionEventBus sessionBus = applicationContext.getBean(EventBus.SessionEventBus.class);
        VaadinSession.setCurrent(new TestSession());
        EventBus.SessionEventBus sessionBus2 = applicationContext.getBean(EventBus.SessionEventBus.class);
        assertNotEquals(sessionBus, sessionBus2, "Different sessions should return different SessionEventBuses");
    }

    @Test
    void sameApplicationReturnsSameApplicationEventBus() {
        EventBus.ApplicationEventBus applicationBus = applicationContext.getBean(EventBus.ApplicationEventBus.class);
        EventBus.ApplicationEventBus applicationBus2 = applicationContext.getBean(EventBus.ApplicationEventBus.class);
        assertEquals(applicationBus, applicationBus2, "ApplicationEventBus should always be the same");
    }

    @Test
    void sameApplicationDifferentUIReturnsSameApplicationEventBus() {
        EventBus.ApplicationEventBus applicationBus = applicationContext.getBean(EventBus.ApplicationEventBus.class);
        UI.setCurrent(createMockUI());
        EventBus.ApplicationEventBus applicationBus2 = applicationContext.getBean(EventBus.ApplicationEventBus.class);
        assertEquals(applicationBus, applicationBus2, "ApplicationEventBus should always be the same");
    }

    @Test
    void sameApplicationDifferentSessionReturnsSameApplicationEventBus() {
        EventBus.ApplicationEventBus applicationBus = applicationContext.getBean(EventBus.ApplicationEventBus.class);
        VaadinSession.setCurrent(new TestSession());
        EventBus.ApplicationEventBus applicationBus2 = applicationContext.getBean(EventBus.ApplicationEventBus.class);
        assertEquals(applicationBus, applicationBus2, "ApplicationEventBus should always be the same");
    }

    /**
     * Creates a new mock UI with a unique ID
     */
    private UI createMockUI() {
        UI ui = Mockito.mock(UI.class);
        int id = uiId++;
        when(ui.getUIId()).thenReturn(id);
        return ui;
    }
}
