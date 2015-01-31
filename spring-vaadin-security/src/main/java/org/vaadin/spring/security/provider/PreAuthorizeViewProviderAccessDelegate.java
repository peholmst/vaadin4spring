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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
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
import org.springframework.security.util.MethodInvocationUtils;
import org.springframework.util.ClassUtils;
import org.vaadin.spring.navigator.SpringViewProvider.ViewProviderAccessDelegate;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;

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
public class PreAuthorizeViewProviderAccessDelegate implements ApplicationContextAware, VaadinSecurityAware, ViewProviderAccessDelegate {

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
    public boolean isAccessGranted(String beanName, UI ui) {

        PreAuthorize viewSecured = applicationContext.findAnnotationOnBean(beanName, PreAuthorize.class);

        if ( viewSecured == null ) {
            return true;
        } else if ( security.hasAccessDecisionManager() ) {

            final Class<?> targetClass = AopUtils.getTargetClass(applicationContext.getBean(beanName));
            final Method method = ClassUtils.getMethod(AopUtils.getTargetClass(applicationContext.getBean(beanName)), "enter", com.vaadin.navigator.ViewChangeListener.ViewChangeEvent.class);
            final MethodInvocation methodInvocation = MethodInvocationUtils.createFromClass(targetClass, method.getName());

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final AccessDecisionManager accessDecisionManager = security.getAccessDecisionManager();
            final ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(new DefaultMethodSecurityExpressionHandler());

            Collection<ConfigAttribute> atributi = new ArrayList<ConfigAttribute>();
            atributi.add(attributeFactory.createPreInvocationAttribute(null, null, viewSecured.value()));

            try {
                accessDecisionManager.decide(authentication, methodInvocation, atributi);
                return true;
            } catch (InsufficientAuthenticationException e) {
                return false;
            } catch (AccessDeniedException e) {
                return false;
            }

        } else {
            return true; // Access decision manager required for @PreAuthorize()
        }

    }

    /*
     * If there is an access manager then the decision is already made 
     */
    @Override
    public boolean isAccessGranted(View view, UI ui) {
        return true;
    }
}
