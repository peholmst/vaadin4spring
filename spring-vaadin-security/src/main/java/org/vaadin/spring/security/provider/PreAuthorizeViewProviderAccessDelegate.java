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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;

/**
 * Implementation of {@link org.vaadin.spring.navigator.SpringViewProvider.ViewProviderAccessDelegate} that
 * checks if a view has the {@link org.springframework.security.access.prepost.PreAuthorize} annotation and if so,
 * uses the {@link org.vaadin.spring.security.VaadinSecurity} instance to check if the current user is authorized to
 * access the view.
 *
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * <br><br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 */
public class PreAuthorizeViewProviderAccessDelegate extends AbstractAnnotationAccessDelegate {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean isAccessGranted(String beanName, UI ui) {

        PreAuthorize viewSecured = applicationContext.findAnnotationOnBean(beanName, PreAuthorize.class);

        if ( viewSecured == null ) {
        	logger.trace("@PreAuthorize annotation not present on view");
            return true;
        } else if ( security.hasAccessDecisionManager() ) {
        	logger.trace("Leave decision to second hook");
    		return true;
        } else {
        	logger.trace("Decision manager is required for @PreAuthorize; defaulting to 'true'");
            return true; // Access decision manager required for @PreAuthorize()
        }

    }

	@Override
	public boolean isAccessGranted(String beanName, UI ui, View view) {
		
		if ( !security.hasAccessDecisionManager() ) {
			return true; // Decision is already done if there is no AccessDecisionManager
		} else {
			return isAccessGrantedForAnnotation(beanName, ui, PreAuthorize.class, "value");
		}
		
	}
}
