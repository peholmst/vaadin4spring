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
package org.vaadin.spring.security.navigation;

import com.vaadin.navigator.View;
import com.vaadin.spring.access.ViewInstanceAccessControl;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link com.vaadin.spring.access.ViewInstanceAccessControl} that
 * checks if a view has the {@link org.springframework.security.access.prepost.PreAuthorize} annotation and if so,
 * uses the {@link org.vaadin.spring.security.VaadinSecurity} instance to check if the current user is authorized to
 * access the view.
 * <br><br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 *
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * @author Petter Holmström (petter@vaadin.com)
 */
public class PreAuthorizeViewInstanceAccessControl implements ApplicationContextAware, VaadinSecurityAware, ViewInstanceAccessControl {

    private static final Logger logger = LoggerFactory.getLogger(PreAuthorizeViewInstanceAccessControl.class);

    private VaadinSecurity security;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
        this.security = vaadinSecurity;
    }

    @Override
    public boolean isAccessGranted(UI ui, String beanName, View view) {
        final PreAuthorize viewSecured = applicationContext.findAnnotationOnBean(beanName, PreAuthorize.class);

        if (viewSecured == null) {
            logger.trace("No @PreAuthorize annotation found on view {}. Granting access.", beanName);
            return true;
        } else if (security.hasAccessDecisionManager()) {
            /*final Class<?> targetClass = AopUtils.getTargetClass(view);
            final Method method = ClassUtils.getMethod(targetClass, "enter", com.vaadin.navigator.ViewChangeListener.ViewChangeEvent.class);
            final MethodInvocation methodInvocation = MethodInvocationUtils.createFromClass(targetClass, method.getName());*/

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final AccessDecisionManager accessDecisionManager = security.getAccessDecisionManager();
            final ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(new DefaultMethodSecurityExpressionHandler());

            final Collection<ConfigAttribute> attributes = Collections.singleton((ConfigAttribute) attributeFactory.createPreInvocationAttribute(null, null, viewSecured.value()));

            try {
                accessDecisionManager.decide(authentication, view, attributes);
                logger.trace("Access to view {} was granted by access decision manager", beanName);
                return true;
            } catch (InsufficientAuthenticationException e) {
                logger.trace("Access to view {} was denied because of insufficient authentication credentials", beanName);
                return false;
            } catch (AccessDeniedException e) {
                logger.trace("Access to view {} was denied", beanName);
                return false;
            }
        } else {
            logger.warn("Found view {} annotated with @PreAuthorize but no access decision manager. Granting access.", beanName);
            return true;
        }
    }
}
