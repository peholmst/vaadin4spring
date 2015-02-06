/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.samples.security.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@VaadinView(name = LoginView.NAME)
@VaadinUIScope
public class LoginView extends VerticalLayout implements View {

    private static final long serialVersionUID = -4430276235082912377L;

    public static final String NAME = "";
    
    @Autowired
    private VaadinSecurity security;
    
    @Autowired
    private HttpService http;
    
    private TextField       username;
    private PasswordField   password;
    private CheckBox        rememberMe = new CheckBox("Remember me", true);
    
    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

    }
    
    
    
    @Override
    public void enter(ViewChangeEvent event) {
        username.focus();
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(rememberMe);
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username = new TextField("Username");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                
                try {
                    
                    security.login(username.getValue(), password.getValue());
                    
                } catch(AuthenticationException e) {
                    e.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                // TODO Register Remember me Token
                
                /*
                 * Redirect is handled by the VaadinRedirectStrategy
                 * User is redirected to either always the default
                 * or the URL the user request before authentication
                 * 
                 * Strategy is configured within SecurityConfiguration
                 * Defaults to User request URL.
                 */
            }
        });
        
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Security-Sample");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

}
