package org.vaadin.spring.boot.sample.security.security;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.boot.security.SelfContainedAuthentication;

/**
 * @author petter@vaadin.com
 */
@VaadinUI(path = "/selfSecured")
@SelfContainedAuthentication
public class SelfAuthenticatingSecuredUI extends UI {
    @Override
    protected void init(VaadinRequest request) {

    }
}
