package org.vaadin.spring.samples.mvp.ui.component.listener;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.samples.mvp.ui.view.BannerView;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Facilitates logout.  Destroys the current session and redirects user to a login page.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@UIScope
@VaadinComponent
public class LogoutLinkListener implements ClickListener {

    private static final long serialVersionUID = -1022565023996117634L;

    @Inject
	private Environment env;

	@Override
	public void buttonClick(ClickEvent event) {
		SecurityContextHolder.clearContext();
		BannerView banner = (BannerView) event.getComponent().getParent();
		String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		String urlMapping = env.getProperty("vaadin.servlet.urlMapping");
		String uiPath = urlMapping.substring(0, urlMapping.length() - 2);
		String location = contextPath.concat(uiPath);
		banner.getUI().getPage().setLocation(location);
	}

}
