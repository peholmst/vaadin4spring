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
package org.vaadin.spring.navigator;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A Vaadin {@link ViewProvider} that fetches the views from the Spring application context. The views
 * must implement the {@link View} interface and be annotated with the {@link VaadinView} annotation.
 * <p/>
 * Use like this:
 * <code>
 *     <pre>
 *         &#64;VaadinUI
 *         public class MyUI extends UI {
 *
 *              &#64;Autowired SpringViewProvider viewProvider;
 *
 *              protected void init(VaadinRequest vaadinRequest) {
 *                  Navigator navigator = new Navigator(this, this);
 *                  navigator.addProvider(viewProvider);
 *                  setNavigator(navigator);
 *                  // ...
 *              }
 *         }
 *     </pre>
 * </code>
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see VaadinView
 */
public class SpringViewProvider implements ViewProvider {

    /*
     * Note! This is a singleton bean!
     */

    // We can have multiple views with the same view name, as long as they belong to different UI subclasses
    private final Map<String, Set<String>> viewNameToBeanNamesMap = new ConcurrentHashMap<>();
    @Autowired
    ApplicationContext applicationContext;
    private transient Log logger = LogFactory.getLog(getClass());

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        logger = LogFactory.getLog(getClass());
    }

    @PostConstruct
    void init() {
        logger.info("Looking up VaadinViews");
        final String[] viewBeanNames = applicationContext.getBeanNamesForAnnotation(VaadinView.class);
        for (String beanName : viewBeanNames) {
            final Class<?> type = applicationContext.getType(beanName);
            if (View.class.isAssignableFrom(type)) {
                final VaadinView annotation = type.getAnnotation(VaadinView.class);
                final String viewName = annotation.name();
                logger.debug(String.format("Found VaadinView bean [%s] with view name [%s]", beanName, viewName));
                Set<String> beanNames = viewNameToBeanNamesMap.get(viewName);
                if (beanNames == null) {
                    beanNames = new ConcurrentSkipListSet<>();
                    viewNameToBeanNamesMap.put(viewName, beanNames);
                }
                beanNames.add(beanName);
            }
        }
    }

    @Override
    public String getViewName(String viewAndParameters) {
        logger.debug(String.format("Extracting view name from [%s]", viewAndParameters));
        String viewName = null;
        if (isViewNameValidForCurrentUI(viewAndParameters)) {
            viewName = viewAndParameters;
        } else {
            int lastSlash = -1;
            String viewPart = viewAndParameters;
            while ((lastSlash = viewPart.lastIndexOf('/')) > -1) {
                viewPart = viewPart.substring(0, lastSlash);
                logger.debug(String.format("Checking if [%s] is a valid view", viewPart));
                if (isViewNameValidForCurrentUI(viewPart)) {
                    viewName = viewPart;
                    break;
                }
            }
        }
        if (viewName == null) {
            logger.debug(String.format("Found no view name in [%s]", viewAndParameters));
        } else {
            logger.debug(String.format("[%s] is a valid view", viewName));
            ;
        }
        return viewName;
    }

    private boolean isViewNameValidForCurrentUI(String viewName) {
        final Set<String> beanNames = viewNameToBeanNamesMap.get(viewName);
        if (beanNames != null) {
            for (String beanName : beanNames) {
                if (isViewBeanNameValidForCurrentUI(beanName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isViewBeanNameValidForCurrentUI(String beanName) {
        try {
            Class<?> type = applicationContext.getType(beanName);

            Assert.isAssignable(View.class, type, "bean did not implement View interface");

            return isViewClassValidForCurrentUI((Class<? extends View>) type);
        } catch (NoSuchBeanDefinitionException ex) {
            return false;
        }
    }

    private boolean isViewClassValidForCurrentUI(Class<? extends View> viewClass) {
        final UI currentUI = UI.getCurrent();
        final VaadinView annotation = viewClass.getAnnotation(VaadinView.class);

        Assert.notNull(annotation, "class did not have a VaadinView annotation");

        if (annotation.ui().length == 0) {
            logger.debug(String.format("View class [%s] with view name [%s] is available for all UI subclasses", viewClass.getCanonicalName(), annotation.name()));
            return true;
        } else {
            for (Class<? extends UI> validUI : annotation.ui()) {
                if (validUI == currentUI.getClass()) {
                    logger.debug(String.format("View class [%s] with view name [%s] is available for UI subclass [%s]", viewClass.getCanonicalName(), annotation.name(), validUI.getCanonicalName()));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public View getView(String viewName) {
        final Set<String> beanNames = viewNameToBeanNamesMap.get(viewName);
        if (beanNames != null) {
            for (String beanName : beanNames) {
                if (isViewBeanNameValidForCurrentUI(beanName)) {
                    return (View) applicationContext.getBean(beanName);
                }
            }
        }
        logger.warn(String.format("Found no view with name [%s]", viewName));
        return null;
    }
}
