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
package org.vaadin.spring.security.navigator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

/**
 * SecuredNavigator implementation.
 * <p>
 * Provides an secured implementation of the {@link Navigator}
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @since 02.06.2015
 */
@Component
@Scope("prototype")
public class SecuredNavigator extends Navigator implements InitializingBean {

    private static final long serialVersionUID = 4581514128602671883L;
    
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    @Autowired
    private SpringViewProvider viewProvider;
    
    @Autowired
    private VaadinSecurity security;
    
    private String defaultAuthenticationView;
    private String notFoundView;
    
    public SecuredNavigator(UI ui, ComponentContainer container) {
        super(ui, container);
    }

    public SecuredNavigator(UI ui, NavigationStateManager stateManager,
            ViewDisplay display) {
        super(ui, stateManager, display);
    }

    public SecuredNavigator(UI ui, SingleComponentContainer container) {
        super(ui, container);
    }

    public SecuredNavigator(UI ui, ViewDisplay display) {
        super(ui, display);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
        /*
         * Check required dependencies
         */
        Assert.notNull(viewProvider, "Failed to autowire <SpringViewProvider>");
        Assert.notNull(security, "Failed to autowire <VaadinSecurity>");
        
        /*
         * Add SpringViewProvider
         */
        addProvider(viewProvider);
    }

    /**
     * Set the view name of the view which contains the authentication view
     * @param the name of the authentication view
     */
    public void setDefaultAuthenticationView(String defaultAuthenticationView) {
        this.defaultAuthenticationView = defaultAuthenticationView;
    }
    
    /**
     * Set the view to navigate to when a view cannot be found.
     * 404 Not Found
     * 
     * @param viewNotFoundView
     */
    public void setNotFoundView(String notFoundView) {
		this.notFoundView = notFoundView;
	}

	@Override
    public void navigateTo(String navigationState) {
        
        log.debug("NavigateTo [{}]", (navigationState.equals("") ? "\"\"" : navigationState));
        
        log.debug("Check if view is valid for current UI");
        if ( viewProvider.isViewNameValidForCurrentUI(navigationState, false) ) {
            
            log.trace("Navigation state is valid, checking access");
            if ( viewProvider.isViewNameValidForCurrentUI(navigationState, true) ) {
                
                log.trace("Access is allowed for requested navigation state");
                super.navigateTo(navigationState);
                
            } else {
                
                log.trace("Access is denied for requested navigation state");
                super.navigateTo(defaultAuthenticationView);
                
            }
            
        } else {
        	
        	log.debug("Requested view is not valid for current UI, redirecting to 404");
            super.navigateTo(notFoundView);
        }
    }
}
