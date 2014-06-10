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
package org.vaadin.spring.stuff.sidebar;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.navigator.VaadinView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods for working with side bars. This class is a Spring managed bean and is mainly
 * intended for internal use.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SideBarUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext applicationContext;

    private final I18N i18n;

    private final List<SideBarSectionDescriptor> sections = new ArrayList<>();

    private final List<SideBarItemDescriptor> items = new ArrayList<>();

    @Autowired
    public SideBarUtils(ApplicationContext applicationContext, I18N i18n) {
        this.applicationContext = applicationContext;
        this.i18n = i18n;
        scanForSections();
        Collections.sort(sections);
        scanForItems();
        Collections.sort(items);
    }

    private void scanForSections() {
        logger.debug("Scanning for side bar sections");
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(SideBarSection.class);
        for (String beanName : beanNames) {
            logger.debug("Bean [{}] declares a side bar section", beanName);
            addSectionDescriptors(applicationContext.findAnnotationOnBean(beanName, SideBarSection.class));
        }
        beanNames = applicationContext.getBeanNamesForAnnotation(SideBarSections.class);
        for (String beanName : beanNames) {
            logger.debug("Bean [{}] declares multiple side bar sections", beanName);
            addSectionDescriptors(applicationContext.findAnnotationOnBean(beanName, SideBarSections.class).value());
        }
    }

    private void addSectionDescriptors(SideBarSection... sections) {
        for (SideBarSection section : sections) {
            logger.debug("Adding side bar section [{}]", section.id());
            this.sections.add(new SideBarSectionDescriptor(section, i18n));
        }
    }

    private void scanForItems() {
        logger.debug("Scanning for side bar items");
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(SideBarItem.class);
        for (String beanName : beanNames) {
            logger.debug("Bean [{}] declares a side bar item", beanName);
            Class<?> beanType = applicationContext.getType(beanName);
            SideBarItem item = beanType.getAnnotation(SideBarItem.class);
            if (Runnable.class.isAssignableFrom(beanType)) {
                logger.debug("Adding side bar item for action [{}]", beanType);
                this.items.add(new SideBarItemDescriptor.ActionItemDescriptor(item, i18n, beanName, applicationContext));
            } else if (View.class.isAssignableFrom(beanType) && beanType.isAnnotationPresent(VaadinView.class)) {
                VaadinView vaadinView = beanType.getAnnotation(VaadinView.class);
                logger.debug("Adding side bar item for view [{}]", vaadinView.name());
                this.items.add(new SideBarItemDescriptor.ViewItemDescriptor(item, i18n, vaadinView));
            }
        }
    }

    /**
     * Gets all side bar sections for the specified UI class.
     *
     * @param uiClass the UI class, must not be {@code null}.
     * @return a collection of side bar section descriptors, never {@code null}.
     * @see SideBarSection#ui()
     */
    public Collection<SideBarSectionDescriptor> getSideBarSections(Class<? extends UI> uiClass) {
        List<SideBarSectionDescriptor> supportedSections = new ArrayList<>();
        for (SideBarSectionDescriptor section : sections) {
            if (section.isAvailableFor(uiClass)) {
                supportedSections.add(section);
            }
        }
        return supportedSections;
    }

    /**
     * Gets all side bar items for the specified side bar section.
     *
     * @param descriptor descriptor the side bar section descriptor, must not be {@code null}.
     * @return a collection of side bar item descriptors, never {@code null}.
     */
    public Collection<SideBarItemDescriptor> getSideBarItems(SideBarSectionDescriptor descriptor) {
        List<SideBarItemDescriptor> supportedItems = new ArrayList<>();
        for (SideBarItemDescriptor item : items) {
            if (item.isMemberOfSection(descriptor)) {
                supportedItems.add(item);
            }
        }
        return supportedItems;
    }

}
