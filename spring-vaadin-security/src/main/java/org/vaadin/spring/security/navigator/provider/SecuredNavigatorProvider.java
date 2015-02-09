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
package org.vaadin.spring.security.navigator.provider;

import org.springframework.util.Assert;
import org.vaadin.spring.security.navigator.SecuredNavigator;
import org.vaadin.spring.security.support.AbstractBeanProvider;

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

/**
 * Provider which creates SecuredNavigator prototype beans
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public class SecuredNavigatorProvider extends AbstractBeanProvider implements NavigatorProvider {

    private static final long serialVersionUID = 441420457274414154L;

    @Override
    public SecuredNavigator getInstance(UI ui, ComponentContainer container) {
        SecuredNavigator navigator = new SecuredNavigator(ui, container);
        return createInstance(navigator);
    }

    @Override
    public SecuredNavigator getInstance(UI ui, SingleComponentContainer container) {
        SecuredNavigator navigator = new SecuredNavigator(ui, container);
        return createInstance(navigator);
    }

    @Override
    public SecuredNavigator getInstance(UI ui, ViewDisplay display) {
        SecuredNavigator navigator = new SecuredNavigator(ui, display);
        return createInstance(navigator);
    }

    @Override
    public SecuredNavigator getInstance(UI ui, NavigationStateManager stateManager, ViewDisplay display) {
        SecuredNavigator navigator = new SecuredNavigator(ui, stateManager, display);
        return createInstance(navigator);
    }
    
    private SecuredNavigator createInstance(SecuredNavigator navigator) {
        
        log.trace("Autowiring new <SecuredNavigator> instance");
        this.autowireFactory.autowireBean(navigator);
        
        log.trace("Creating Spring beanName for new <SecuredNavigator> instance");
        String beanName = generateBeanName("securedNavigator-", navigator);
        
        log.trace("Initializing <SecuredNavigator> instance '{}' in spring container", beanName);
        this.autowireFactory.initializeBean(navigator, beanName);
        
        Assert.notNull(navigator);
        
        return navigator;
    }
}
