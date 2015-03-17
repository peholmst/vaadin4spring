package org.vaadin.spring.portlet;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinPortlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

import javax.portlet.PortletException;

/**
 * Subclass of the standard {@link com.vaadin.server.VaadinPortlet Vaadin portlet} that adds a
 * {@link org.vaadin.spring.portlet.SpringAwareUIProvider} to every new Vaadin session.
 *
 * <p>If you need a custom Vaadin portlet, you can either extend this portlet directly, or extend another subclass of
 * {@link com.vaadin.server.VaadinServlet} and just add the UI provider.</p>
 */
public class SpringAwareVaadinPortlet extends VaadinPortlet {
    private static final long serialVersionUID = 481506148953544624L;

    @Override
    protected void portletInitialized() throws PortletException {
        getService().addSessionInitListener(new SessionInitListener() {
            private static final long serialVersionUID = -4571879262155039969L;

            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                try {
                    ApplicationContext context = PortletApplicationContextUtils
                            .getRequiredWebApplicationContext(getPortletContext());
                    SpringAwareUIProvider uiProvider = new SpringAwareUIProvider(context);
                    event.getSession().addUIProvider(uiProvider);
                } catch (IllegalStateException e) {
                    throw new ServiceException(e);
                }
            }
        });
    }
}
