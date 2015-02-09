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

import java.io.Serializable;

import org.vaadin.spring.security.navigator.SecuredNavigator;

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

/**
 * Interface to provide access to the SecuredNavigatorProvider 
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public interface NavigatorProvider extends Serializable {

    SecuredNavigator getInstance(UI ui, ComponentContainer container);
    
    SecuredNavigator getInstance(UI ui, SingleComponentContainer container);
    
    SecuredNavigator getInstance(UI ui, ViewDisplay display);
    
    SecuredNavigator getInstance(UI ui, NavigationStateManager stateManager, ViewDisplay display);
}
