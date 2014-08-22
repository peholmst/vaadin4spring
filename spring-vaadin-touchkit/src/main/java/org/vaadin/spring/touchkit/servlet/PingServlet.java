package org.vaadin.spring.touchkit.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responds to /PING requests done by TouchKit to workaround
 * cordova-android-offlinemode issues.
 */
public class PingServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.setHeader("Cache-Control",
                "no-store, no-cache, max-age=0, must-revalidate");
        resp.getWriter().println();
    }

}
