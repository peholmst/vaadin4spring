package org.vaadin.spring.samples.security.single;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by petterwork on 08/06/15.
 */
@Component
public class VaadinSecurityContextRequestHandler implements RequestHandler {

    @Override
    public boolean handleRequest(VaadinSession vaadinSession, VaadinRequest vaadinRequest, VaadinResponse vaadinResponse) throws IOException {
        vaadinSession.lock();
        try {
            System.out.println("handling request");
        } finally {
            vaadinSession.unlock();
        }
        return false;
    }
}
