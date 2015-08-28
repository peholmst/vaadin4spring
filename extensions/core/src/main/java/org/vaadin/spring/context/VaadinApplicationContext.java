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
package org.vaadin.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * VaadinApplicationContext allows static access to the {@link org.springframework.context.ApplicationContext}.
 * This implementation exists to provide access from non-managed spring beans.
 * <p>A VaadinApplicationContext provides:
 * <ul>
 * <li>Access to the Spring {@link org.springframework.context.ApplicationContext}.
 * </ul>
 *
 * @author G.J.R. Timmer
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.springframework.context.ApplicationContext
 */
public class VaadinApplicationContext implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(VaadinApplicationContext.class);

    private static ApplicationContext context;

    /**
     * Return the spring {@link org.springframework.context.ApplicationContext}
     *
     * @return the spring {@link org.springframework.context.ApplicationContext}
     */
    public static ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (VaadinApplicationContext.class) {
            context = applicationContext;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("{} initialized", getClass().getName());
    }
}
