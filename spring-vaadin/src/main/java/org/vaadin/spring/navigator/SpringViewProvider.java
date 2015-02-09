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
package org.vaadin.spring.navigator;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.Assert;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.spring.navigator.internal.VaadinViewScope;
import org.vaadin.spring.navigator.internal.ViewCache;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;

public class SpringViewProvider extends AbstractVaadinViewProvider  {

	private static final long serialVersionUID = -6137982522876664559L;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String getViewName(String viewAndParameters) {
		
		log.trace("Extracting view name from [{}]", viewAndParameters);
		String viewName = null;
		if ( isViewNameValidForCurrentUI(viewAndParameters, true) ) {
			viewName = viewAndParameters;
		}
		
		if ( viewName == null ) {
			log.trace("Found no view name in [{}]", viewAndParameters);
		} else {
			log.trace("[{}] is a valid view for current UI", viewName);
		}
		
		return viewName;
	}
	
	/**
	 * Check if a viewName is valid for this UI
	 * 
	 * @param viewName
	 * @return <code>true</code> if view is valid
	 * <code>false</code> if view is not valid for current UI.
	 */
	public boolean isViewNameValidForCurrentUI(String viewAndParameters, boolean checkAccess) {
        
		Set<String> beanNames = vaadinViewMap.get(viewAndParameters);
        if (beanNames != null) {
        	for (String beanName : beanNames) {
                if (isViewBeanNameValidForCurrentUI(beanName, checkAccess)) {
                    return true;
                }
            }
        } else {
        	
        	int lastSlash = -1;
            String viewPart = viewAndParameters;
            while ((lastSlash = viewPart.lastIndexOf('/')) > -1) {
                viewPart = viewPart.substring(0, lastSlash);
                log.trace("Checking if [{}] is a valid view", viewPart);
                beanNames = vaadinViewMap.get(viewAndParameters);
                if (beanNames != null) {
                	for (String beanName : beanNames) {
                        if (isViewBeanNameValidForCurrentUI(beanName, checkAccess)) {
                            return true;
                        }
                    }
                }
            }
        	
        }
		
        return false;
    }
	
	private boolean isViewBeanNameValidForCurrentUI(String beanName, boolean checkAccess) {
        try {
            final Class<?> type = applicationContext.getType(beanName);

            Assert.isAssignable(View.class, type, "bean did not implement View interface");

            final UI currentUI = UI.getCurrent();
            final VaadinView annotation = applicationContext.findAnnotationOnBean(beanName, VaadinView.class);

            Assert.notNull(annotation, "class did not have a VaadinView annotation");

            if (checkAccess) {
            	final Map<String, ViewProviderAccessDelegate> accessDelegates = applicationContext.getBeansOfType(ViewProviderAccessDelegate.class);
                for (ViewProviderAccessDelegate accessDelegate : accessDelegates.values()) {
                    if (!accessDelegate.isAccessGranted(beanName, currentUI)) {
                        log.debug("Access delegate [{}] denied access to view class [{}]", accessDelegate, type.getCanonicalName());
                        return false;
                    }
                }
            }

            if (annotation.ui().length == 0) {
                log.trace("View class [{}] with view name [{}] is available for all UI subclasses", type.getCanonicalName(), annotation.name());
                return true;
            } else {
                for (Class<? extends UI> validUI : annotation.ui()) {
                    if (validUI == currentUI.getClass()) {
                        log.trace("View class [%s] with view name [{}] is available for UI subclass [{}]", type.getCanonicalName(), annotation.name(), validUI.getCanonicalName());
                        return true;
                    }
                }
            }
            return false;
        } catch (NoSuchBeanDefinitionException ex) {
            return false;
        }
    }

	@Override
    public View getView(String viewName) {
        final Set<String> beanNames = vaadinViewMap.get(viewName);
        if (beanNames != null) {
            for (String beanName : beanNames) {
                if (isViewBeanNameValidForCurrentUI(beanName, true)) {
                    return getViewFromApplicationContext(viewName, beanName);
                }
            }
        }
        log.warn("Found no view with name [{}]", viewName);
        return null;
    }

    private View getViewFromApplicationContext(String viewName, String beanName) {
        BeanDefinition beanDefinition = beandefinitionRegistry.getBeanDefinition(beanName);
        if (beanDefinition.getScope().equals(VaadinViewScope.VAADIN_VIEW_SCOPE_NAME)) {
            log.trace("View [{}] is view scoped, activating scope", viewName);
            final ViewCache viewCache = applicationContext.getBean(ViewCache.class);
            viewCache.creatingView(viewName);
            View view = null;
            try {
                view = getViewFromApplicationContextAndCheckAccess(beanName);
                return view;
            } finally {
                viewCache.viewCreated(viewName, view);
            }
        } else {
            return getViewFromApplicationContextAndCheckAccess(beanName);
        }
    }

    private View getViewFromApplicationContextAndCheckAccess(String beanName) {
        final View view = (View) applicationContext.getBean(beanName);
        if (isAccessGrantedToViewInstance(view)) {
            return view;
        } else {
            return null;
        }
    }


    private boolean isAccessGrantedToViewInstance(View view) {
        final UI currentUI = UI.getCurrent();
        final Map<String, ViewProviderAccessDelegate> accessDelegates = applicationContext.getBeansOfType(ViewProviderAccessDelegate.class);
        for (ViewProviderAccessDelegate accessDelegate : accessDelegates.values()) {
            if (!accessDelegate.isAccessGranted(view, currentUI)) {
                log.debug("Access delegate [{}] denied access to view [{}]", accessDelegate, view);
                return false;
            }
        }
        return true;
    }

}
