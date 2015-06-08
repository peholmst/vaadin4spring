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
package org.vaadin.spring.security.provider;

import com.vaadin.navigator.View;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.access.ViewInstanceAccessControl;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;

/**
 * Implementation of {@link com.vaadin.spring.access.ViewAccessControl} and {@link com.vaadin.spring.access.ViewInstanceAccessControl} that
 * checks if a view has the {@link org.springframework.security.access.annotation.Secured} annotation and if so,
 * uses the {@link org.vaadin.spring.security.VaadinSecurity} instance to check if the current user is authorized to
 * access the view.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see VaadinSecurity#hasAnyAuthority(String...)
 */
public class SecuredViewAccessControl implements VaadinSecurityAware, ApplicationContextAware, ViewAccessControl, ViewInstanceAccessControl {

    private static final Logger logger = LoggerFactory.getLogger(SecuredViewAccessControl.class);

    private VaadinSecurity security;
    private ApplicationContext applicationContext;

    @Override
    public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
        security = vaadinSecurity;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        final Secured viewSecured = applicationContext.findAnnotationOnBean(beanName, Secured.class);

        if (viewSecured == null) {
            logger.trace("No @Secured annotation found on view {}. Granting access.", beanName);
            return true;
        } else if (security.hasAccessDecisionManager()) {
            logger.trace("AccessDecisionManager found, leaving access decision concerning view {} to the instance access control", beanName);
            return true;
        } else {
            final boolean result = security.hasAnyAuthority(viewSecured.value());
            logger.trace("Is access granted to view {}: {}", beanName, result);
            return result;
        }
    }

    @Override
    public boolean isAccessGranted(UI ui, String beanName, View view) {
        final Secured viewSecured = applicationContext.findAnnotationOnBean(beanName, Secured.class);

        if (viewSecured == null || !security.hasAccessDecisionManager()) {
            logger.trace("No @Secured annotation found or no AccessDecisionManager found. Granting access to view {}", beanName);
            return true; // Decision is already done if there is no AccessDecisionManager
        } else {
            final boolean result = security.hasAccessToSecuredObject(view);
            logger.trace("Is access granted to view {}: {}", beanName, result);
            return result;
        }
    }
}
