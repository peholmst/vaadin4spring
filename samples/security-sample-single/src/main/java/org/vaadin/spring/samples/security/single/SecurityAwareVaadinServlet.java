package org.vaadin.spring.samples.security.single;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.spring.server.SpringVaadinServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;

/**
 * Created by petterwork on 08/06/15.
 */
@Component
public class SecurityAwareVaadinServlet extends SpringVaadinServlet {

    final VaadinSecurityContextRequestHandler vaadinSecurityContextRequestHandler;

    @Autowired
    public SecurityAwareVaadinServlet(VaadinSecurityContextRequestHandler vaadinSecurityContextRequestHandler) {
        this.vaadinSecurityContextRequestHandler = vaadinSecurityContextRequestHandler;
    }

    @Override
    protected void servletInitialized() throws ServletException {
        getService().addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
                sessionInitEvent.getSession().addRequestHandler(vaadinSecurityContextRequestHandler);
            }
        });
    }
}
