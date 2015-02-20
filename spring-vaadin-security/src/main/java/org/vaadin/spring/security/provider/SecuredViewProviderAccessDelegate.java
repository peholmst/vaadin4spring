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
import com.vaadin.ui.UI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.navigator.SpringViewProvider.ViewProviderAccessDelegate;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;

/**
 * Implementation of {@link org.vaadin.spring.navigator.SpringViewProvider.ViewProviderAccessDelegate} that
 * checks if a view has the {@link org.springframework.security.access.annotation.Secured} annotation and if so,
 * uses the {@link org.vaadin.spring.security.VaadinSecurity} instance to check if the current user is authorized to
 * access the view.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @see VaadinSecurity#hasAnyAuthority(String...)
 */
public class SecuredViewProviderAccessDelegate implements VaadinSecurityAware, ViewProviderAccessDelegate {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    private VaadinSecurity security;
    private ApplicationContext applicationContext;

    @Override
    public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
        security = vaadinSecurity;
        applicationContext = security.getApplicationContext();
    }

    @Override
    public boolean isAccessGranted(String beanName, UI ui) {

        Secured viewSecured = applicationContext.findAnnotationOnBean(beanName, Secured.class);

        if ( viewSecured == null ) {
        	logger.trace("@Secured annotation not present on view");
            return true;
        } else if ( security.hasAccessDecisionManager() ) {
        	logger.trace("Second hook will handle decision");
            return true; // Leave decision to the second hook
        } else {
        	logger.trace("Check authority");
            return security.hasAnyAuthority(viewSecured.value());
        }
    }

    @Override
    public boolean isAccessGranted(View view, UI ui) {
        logger.trace("Instance: {} | Class: {} | IsAnnotated: {}", view.toString(), view.getClass(), view.getClass().getAnnotation(Secured.class));
        
        Secured viewSecured = view.getClass().getAnnotation(Secured.class);
        if ( AopUtils.isJdkDynamicProxy(view) ) {
        	try {
        		viewSecured = ((Advised) view).getTargetSource().getTarget().getClass().getAnnotation(Secured.class);
        	} catch(Exception e) {
        		e.printStackTrace();
        		viewSecured = null;
        	}
        	
        	/*
        	Object proxyView = view;
            while( AopUtils.isJdkDynamicProxy(proxyView) ) {
            	proxyView = AopUtils.getTargetClass(proxyView);
            }
            viewSecured = ((Advised) proxyView).get
            Class<?> secured = proxyView.getClass();
            if ( secured.isAnnotationPresent(Secured.class) ) {
            	logger.trace("@Secured present on view");
            }
            */
            //viewSecured = proxyView.getClass().is getAnnotation(Secured.class);
        }

        if ( viewSecured == null || !security.hasAccessDecisionManager() ) {
            logger.trace("Decision already done or no decision manager present");
        	return true; // Decision is already done if there is no AccessDecisionManager
        } else {
        	logger.trace("Requesting decision from decisionmanager");
            try {
				return security.hasAccessToSecuredObject(view);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return true;
        }
    }
}
