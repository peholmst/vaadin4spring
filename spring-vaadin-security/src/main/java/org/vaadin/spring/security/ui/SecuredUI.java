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
package org.vaadin.spring.security.ui;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;
import org.vaadin.spring.security.navigator.SecuredNavigator;
import org.vaadin.spring.security.navigator.provider.SecuredNavigatorProvider;
import org.vaadin.spring.security.navigator.provider.SecuredNavigatorProviderAware;

import com.vaadin.ui.UI;

/**
 * Abstract class to provide a SecuredUI which can be extended to implement a secured UI.
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @since 02.06.2015
 */
public abstract class SecuredUI extends UI implements InitializingBean, ApplicationContextAware, VaadinSecurityAware, SecuredNavigatorProviderAware {

    private static final long serialVersionUID = -5361641475528969012L;
    private SecuredNavigatorProvider navigatorProvider;
    
    protected VaadinSecurity security;
    protected ApplicationContext applicationContext;
    
    @Override
    public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
        this.security = vaadinSecurity;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void setSecuredNavigatorProvider(SecuredNavigatorProvider provider) {
        this.navigatorProvider = provider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        
        Assert.notNull(applicationContext);
        Assert.notNull(security);
        Assert.notNull(navigatorProvider);
        
        Assert.notNull(defaultAuthenticationView(), "Default authentication view cannot be null");
        Assert.notNull(notFoundView(), "Default fallback view cannot be null");
        
        SecuredNavigator navigator = navigatorProvider.getInstance(this, this);
        navigator.setDefaultAuthenticationView(defaultAuthenticationView());
        navigator.setNotFoundView(notFoundView());
        setNavigator(navigator);
    }

    /**
     * Set the default authentication view
     * <p>
     * Redirect the user to the default authentication view
     * if access to the request view is not granted
     * 
     * @return {@link String} default authentication view name
     */
    public abstract String defaultAuthenticationView();
    
    /**
     * Set the 404 Not Found view.
     * <p>
     * Redirect the user to this view if a view is requested which
     * does not belong to the current UI or a view that does not exists.
     * 
     * @return {@link String} view name of "404 Not Found View"
     */
    public abstract String notFoundView();
}
