/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.security;

import com.vaadin.ui.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.navigator.SpringViewProvider;

/**
 * Implementation of {@link org.vaadin.spring.navigator.SpringViewProvider.ViewProviderAccessDelegate} that
 * checks if a view has the {@link org.springframework.security.access.annotation.Secured} annotation and if so,
 * uses the {@link org.vaadin.spring.security.Security} instance to check if the current user is authorized to
 * access the view.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see Security#hasAnyAuthority(String...)
 */
public class SpringSecurityViewProviderAccessDelegate implements SpringViewProvider.ViewProviderAccessDelegate {

    private final Security security;
    private final ApplicationContext applicationContext;

    @Autowired
    public SpringSecurityViewProviderAccessDelegate(Security security, ApplicationContext applicationContext) {
        this.security = security;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isAccessGranted(String beanName, UI ui) {
    	Object securedObject = applicationContext.getBean(beanName);
        Secured viewSecured = applicationContext.findAnnotationOnBean(beanName, Secured.class);
        
        return !(viewSecured != null && !security.hasAccessToSecuredObject(securedObject));
    }
}
