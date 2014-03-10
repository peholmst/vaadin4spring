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
package org.vaadin.spring.touchkit.servlet;

import com.vaadin.ui.UI;
import org.springframework.web.context.WebApplicationContext;
import org.vaadin.spring.servlet.internal.AbstractSpringAwareUIProvider;
import org.vaadin.spring.touchkit.TouchKitUI;

/**
 * Vaadin {@link com.vaadin.server.UIProvider} that looks up UI classes from the Spring application context. The UI
 * classes must be annotated with {@link org.vaadin.spring.touchkit.TouchKitUI}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SpringAwareTouchKitUIProvider extends AbstractSpringAwareUIProvider {

    public SpringAwareTouchKitUIProvider(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    protected void detectUIs() {
        logger.info("Checking the application context for TouchKit UIs");
        final String[] uiBeanNames = getWebApplicationContext().getBeanNamesForAnnotation(TouchKitUI.class);
        for (String uiBeanName : uiBeanNames) {
            Class<?> beanType = getWebApplicationContext().getType(uiBeanName);
            if (UI.class.isAssignableFrom(beanType)) {
                logger.info("Found TouchKit UI [{}]", beanType.getCanonicalName());
                final String path = getWebApplicationContext().findAnnotationOnBean(uiBeanName, TouchKitUI.class).path();
                Class<? extends UI> existingBeanType = getUIByPath(path);
                if (existingBeanType != null) {
                    throw new IllegalStateException(String.format("[%s] is already mapped to the path [%s]", existingBeanType.getCanonicalName(), path));
                }
                logger.debug("Mapping TouchKit UI [{}] to path [{}]", beanType.getCanonicalName(), path);
                mapPathToUI(path, (Class<? extends UI>) beanType);
            }
        }
    }
}
