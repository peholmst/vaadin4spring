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
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.navigator.VaadinView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO Document me!
 *
 * @author petter@vaadin.com
 */
public class SideBarUtils {

    private final ApplicationContext applicationContext;

    private final I18N i18n;

    private final List<SideBarSectionDescriptor> sections = new ArrayList<>();

    private final List<SideBarItemDescriptor> items = new ArrayList<>();

    public SideBarUtils(ApplicationContext applicationContext, I18N i18n) {
        this.applicationContext = applicationContext;
        this.i18n = i18n;
        scanForSections();
        Collections.sort(sections);
        scanForItems();
        Collections.sort(items);
    }

    private void scanForSections() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(SideBarSection.class);
        for (String beanName : beanNames) {
            addSectionDescriptors(applicationContext.getType(beanName).getAnnotation(SideBarSection.class));
        }
        beanNames = applicationContext.getBeanNamesForAnnotation(SideBarSections.class);
        for (String beanName : beanNames) {
            addSectionDescriptors(applicationContext.getType(beanName).getAnnotation(SideBarSections.class).value());
        }
    }

    private void addSectionDescriptors(SideBarSection... sections) {
        for (SideBarSection section : sections) {
            this.sections.add(new SideBarSectionDescriptor(section, i18n));
        }
    }

    private void scanForItems() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(SideBarItem.class);
        for (String beanName : beanNames) {
            Class<?> beanType = applicationContext.getType(beanName);
            SideBarItem item = beanType.getAnnotation(SideBarItem.class);
            if (Runnable.class.isAssignableFrom(beanType)) {
                this.items.add(new SideBarItemDescriptor.ActionItemDescriptor(item, i18n, beanName, applicationContext));
            } else if (View.class.isAssignableFrom(beanType) && beanType.isAnnotationPresent(VaadinView.class)) {
                VaadinView vaadinView = beanType.getAnnotation(VaadinView.class);
                this.items.add(new SideBarItemDescriptor.ViewItemDescriptor(item, i18n, vaadinView));
            }
        }
    }

    /**
     * @param uiClass
     * @return
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
     * @param descriptor
     * @return
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
