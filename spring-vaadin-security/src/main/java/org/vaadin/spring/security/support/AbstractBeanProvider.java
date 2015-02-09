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
package org.vaadin.spring.security.support;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Helper class to manually create, autowire and initialize beans within the application context
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
public abstract class AbstractBeanProvider implements InitializingBean, ApplicationContextAware {
    
    protected final Logger                          log = LoggerFactory.getLogger(this.getClass().getName());
    protected ApplicationContext                    applicationContext;
    protected AutowireCapableBeanFactory            autowireFactory;
    protected AutowiredAnnotationBeanPostProcessor  postProcessor;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        
        Assert.notNull(applicationContext);
        this.autowireFactory = applicationContext.getAutowireCapableBeanFactory();
        
    }

    /**
     * Get Beans by type
     * 
     * @param clazz Class to fetch from application context
     * @return Class
     * @throws UnsupportedOperationException
     */
    protected <T> T getBeansByType(final Class<T> clazz) throws UnsupportedOperationException {
        Map<String, T> beansOfType = this.applicationContext.getBeansOfType(clazz);
        
        final int size = beansOfType.size();
        
        switch(size) {
            case 0:
            default:
                throw new UnsupportedOperationException("No bean found of type: " + clazz);
                
            case 1:
                String name = (String) beansOfType.keySet().iterator().next();
                return clazz.cast(this.applicationContext.getBean(name, clazz));
        }
    }
    
    protected <T> T getBeansByType(final Class<T> clazz, Object ... args) throws UnsupportedOperationException {
        Map<String, T> beansOfType = this.applicationContext.getBeansOfType(clazz);
        
        final int size = beansOfType.size();
        
        switch(size) {
            case 0:
            default:
                throw new UnsupportedOperationException("No bean found of type: " + clazz);
                
            case 1:
                String name = (String) beansOfType.keySet().iterator().next();
                return clazz.cast(this.applicationContext.getBean(name, args));
        }
    }
    
    /**
     * Generate bean name combined with prefix
     * @param prefix for beanname
     * @param obj to generate bean name for
     * @return bean name
     */
    protected String generateBeanName(final String prefix, Object obj) {
        
        Assert.notNull(obj);
        
        StringBuilder beanName = new StringBuilder(prefix);
        beanName.append(obj.hashCode());
        
        return beanName.toString();
    }
}
