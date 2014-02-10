package org.vaadin.spring.boot.sample.security.security;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.boot.security.ExternalUIAuthentication;

/**
 * @author petter@vaadin.com
 */
@VaadinUI(path = "/secured")
@ExternalUIAuthentication(authenticationUI = LoginUI.class)
@Secured({Roles.USER, Roles.ADMIN})
public class SecuredUI extends UI {

    @Autowired
    SecuredUIContent uiContent;

    @Override
    protected void init(VaadinRequest request) {
        setContent(uiContent);
    }
}
