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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.MethodInvocationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.vaadin.spring.navigator.ViewProviderAccessDelegate;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityAware;

import com.vaadin.ui.UI;

/**
 * Abstract implementation to provide easy handling of annotation secured objects
 * 
 * @author Marko Radinovic (markoradinovic79@gmail.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 * <br><br>
 * Initial code:<a href="https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity">https://github.com/markoradinovic/Vaadin4Spring-MVP-Sample-SpringSecurity</a>
 */
public abstract class AbstractAnnotationAccessDelegate implements ApplicationContextAware, VaadinSecurityAware, InitializingBean, ViewProviderAccessDelegate {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected ApplicationContext applicationContext;
	protected VaadinSecurity security;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;		
	}
	
	@Override
	public void setVaadinSecurity(VaadinSecurity vaadinSecurity) {
		this.security = vaadinSecurity;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.trace("> afterPropertiesSet()");
		
		Assert.notNull(applicationContext);
		Assert.notNull(security);
		
		logger.trace("< afterPropertiesSet()");
	}
	
	/**
	 * Helper class which decides if a object is annotated and configures the access decision manager
	 * with the correct values.
	 * 
	 * @param beanName {@link String} bean name
	 * @param ui {@link UI} UI instance
	 * @param annotation Annotation class which to check
	 * @param annotationAttribute Annotation attribute which contains the arguments for the access decision manager
	 * 
	 * @return <code>true</code> when access is granted by the access decision manager, <code>false</code> when access is denied by the access
	 * decision manager.
	 */
	protected boolean isAccessGrantedForAnnotation(String beanName, UI ui, Class<? extends Annotation> annotation, String annotationAttribute) {
		
		Annotation annotationClass = applicationContext.findAnnotationOnBean(beanName, annotation);
		
		if ( annotationClass == null ) {
        	logger.trace("{} annotation not present on view", annotation.getSimpleName());
            return true;
        } else if ( security.hasAccessDecisionManager() ) {

        	logger.trace("DecisionManager present");
        	
            final Class<?> targetClass = AopUtils.getTargetClass(applicationContext.getBean(beanName));
            final Method method = ClassUtils.getMethod(AopUtils.getTargetClass(applicationContext.getBean(beanName)), "enter", com.vaadin.navigator.ViewChangeListener.ViewChangeEvent.class);
            final MethodInvocation methodInvocation = MethodInvocationUtils.createFromClass(targetClass, method.getName());

            final Authentication authentication = security.getAuthentication();
            final AccessDecisionManager accessDecisionManager = security.getAccessDecisionManager();
            final ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(new DefaultMethodSecurityExpressionHandler());

            Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
            
            try {
            	
            	logger.trace("Fetching value() from annotation");
            	Object annotationValue = getAnnotationValue(targetClass, annotation, annotationAttribute);
            	if ( annotationValue instanceof String[] ) {
            		for(String securityValue : (String[]) annotationValue) {
            			attributes.add(new SecurityConfig(securityValue));
            		}
            	} else {
            		attributes.add(attributeFactory.createPreInvocationAttribute(null, null, annotationValue.toString()));
            	}

            	logger.trace("Requesting decision from decisionmanager");
                accessDecisionManager.decide(authentication, methodInvocation, attributes);
                return true;
            } catch (InsufficientAuthenticationException e) {
                return false;
            } catch (AccessDeniedException e) {
                return false;
            } catch (Exception e) {
				e.printStackTrace();
				return false;
			}

        } else {
        	logger.trace("Decision manager is required for @PreAuthorize; defaulting to 'true'");
            return true; // Access decision manager required for @PreAuthorize()
        }
		
	}
	
	@SuppressWarnings("unchecked")
	protected static<T> T getAnnotationValue(Class<?> clazz, Class<? extends Annotation> annotationClass, String element) throws Exception {
		Annotation annotation = clazz.getAnnotation(annotationClass);
		Method method = annotationClass.getMethod(element, (Class[]) null);
		if ( annotation == null ) {
			return (T) method.getDefaultValue();
		} else {
			return (T) method.invoke(annotation, (Object[]) null);
		}
	}
	
}
