package org.vaadin.spring.integration;

import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.FutureAccess;
import com.vaadin.flow.server.ServiceDestroyListener;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.SessionExpiredException;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.SystemMessagesProvider;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.VaadinSessionState;
import com.vaadin.flow.server.WrappedSession;
import com.vaadin.flow.spring.SpringVaadinSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.web.context.WebApplicationContext;
import org.vaadin.spring.request.VaadinRequestEndListener;
import org.vaadin.spring.request.VaadinRequestStartListener;
import org.vaadin.spring.servlet.CustomInitParameterProvider;
import org.vaadin.spring.servlet.Vaadin4SpringServlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the different aspects of the {@link org.vaadin.spring.servlet.Vaadin4SpringServlet}
 *
 * @author erik@vaadin.com
 * @since 15/05/2019
 */
@SpringJUnitWebConfig(Vaadin4SpringServletIntegrationTest.TestConfiguration.class)
public class Vaadin4SpringServletIntegrationTest {

    private TestServlet testServlet;

    private static SystemMessagesProvider systemMessagesProvider = Mockito.mock(SystemMessagesProvider.class);
    private static SessionInitListener sessionInitListener = Mockito.mock(SessionInitListener.class);
    private static SessionDestroyListener sessionDestroyListener = Mockito.mock(SessionDestroyListener.class);
    private static ServiceDestroyListener serviceDestroyListener = Mockito.mock(ServiceDestroyListener.class);
    private static VaadinRequestStartListener vaadinRequestStartListener = Mockito.mock(VaadinRequestStartListener.class);
    private static VaadinRequestEndListener vaadinRequestEndListener = Mockito.mock(VaadinRequestEndListener.class);
    private static CustomInitParameterProvider customInitParameterProvider = Mockito.mock(CustomInitParameterProvider.class);

    @Mock private VaadinServletRequest vaadinRequest;
    @Mock private VaadinServletResponse vaadinResponse;
    @Mock private SpringVaadinSession vaadinSession;
    @Mock private WrappedSession wrappedSession;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() throws ServletException {
        MockitoAnnotations.initMocks(this);

        when(vaadinRequest.getWrappedSession()).thenReturn(wrappedSession);
        when(vaadinRequest.getWrappedSession(anyBoolean())).thenReturn(wrappedSession);
        when(vaadinRequest.getAttribute("requestStartTime")).thenReturn(0L);

        when(wrappedSession.getAttribute(".lock")).thenReturn(new ReentrantLock());
        when(wrappedSession.getAttribute("com.vaadin.flow.server.VaadinSession.")).thenReturn(null);

        testServlet = new TestServlet(webApplicationContext,  false);
        testServlet.init(new MockServletConfig() {
            @Override
            public ServletContext getServletContext() {
                return webApplicationContext.getServletContext();
            }
        });

        Mockito.reset(systemMessagesProvider, sessionInitListener, sessionDestroyListener, serviceDestroyListener,
                vaadinRequestStartListener, vaadinRequestEndListener, customInitParameterProvider);
    }

    @Test
    public void testCustomSystemMessagesProvider() {
        assertEquals(systemMessagesProvider, testServlet.getService().getSystemMessagesProvider());
    }

    @Test
    public void testSessionInitListener() throws SessionExpiredException, ServiceException {
        testServlet.getService().findVaadinSession(vaadinRequest);
        verify(sessionInitListener).sessionInit(any());
    }

    @Test
    public void testSessionDestroyListener() {
        VaadinSession.setCurrent(vaadinSession);
        when(vaadinSession.getState()).thenReturn(VaadinSessionState.CLOSING);
        when(vaadinSession.access(any(Command.class))).then(invocation -> {
            Command command = (Command) invocation.getArguments()[0];
            FutureAccess future = new FutureAccess(vaadinSession, command);
            command.execute();
            return future;
        });
        testServlet.getService().requestEnd(vaadinRequest, vaadinResponse, vaadinSession);
        verify(sessionDestroyListener).sessionDestroy(any());
    }

    @Test
    public void testServiceDestroyListener() {
        testServlet.getService().destroy();
        verify(serviceDestroyListener).serviceDestroy(any());
    }

    @Test
    public void testRequestStart() {
        // Let any exception be thrown due to the all aspects of the request not being completely mocked,
        // as long as the request start listener is called
        assertThrows(Exception.class, () ->
                testServlet.getService().handleRequest(vaadinRequest, vaadinResponse));
        verify(vaadinRequestStartListener).onRequestStart(vaadinRequest, vaadinResponse);
    }

    @Test
    public void testRequestEnd() {
        // Let any exception be thrown due to the all aspects of the request not being completely mocked,
        // as long as the request end listener is called
        assertThrows(Exception.class, () ->
                testServlet.getService().handleRequest(vaadinRequest, vaadinResponse));
        verify(vaadinRequestEndListener).onRequestEnd(eq(vaadinRequest), eq(vaadinResponse), any());
    }

    @Test
    public void testCustomInitParameters() {
        testServlet.getServletConfig().getInitParameterNames();
        verify(customInitParameterProvider).getInitParameterNames();
    }

    @WebServlet
    public class TestServlet extends Vaadin4SpringServlet {
        public TestServlet(ApplicationContext context, boolean forwardingEnforced) {
            super(context, forwardingEnforced);
        }
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        public SystemMessagesProvider getSystemMessagesProvider() {
            return systemMessagesProvider;
        }
        @Bean
        public SessionInitListener getSessionInitListener() {
            return sessionInitListener;
        }
        @Bean
        public SessionDestroyListener getSessionDestroyListener() {
            return sessionDestroyListener;
        }
        @Bean
        public ServiceDestroyListener getServiceDestroyListener() {
            return serviceDestroyListener;
        }
        @Bean
        public VaadinRequestStartListener getVaadinRequestStartListener() {
            return vaadinRequestStartListener;
        }
        @Bean
        public VaadinRequestEndListener getVaadinRequestEndListener() {
            return vaadinRequestEndListener;
        }
        @Bean
        public CustomInitParameterProvider getCustomInitParameterProvider() {
            return customInitParameterProvider;
        }

    }
}
