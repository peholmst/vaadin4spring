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

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.UI;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.navigator.VaadinView;

/**
 * TODO Document me!
 *
 * @author petter@vaadin.com
 */
public abstract class SideBarItemDescriptor implements Comparable<SideBarItemDescriptor> {

    private final SideBarItem item;
    private final I18N i18n;

    /**
     * @param item
     * @param i18n
     */
    public SideBarItemDescriptor(SideBarItem item, I18N i18n) {
        this.item = item;
        this.i18n = i18n;
    }

    /**
     * @return
     */
    public String getCaption() {
        if (item.captionCode().isEmpty()) {
            return item.caption();
        } else {
            return i18n.get(item.captionCode());
        }
    }

    /**
     * @return
     */
    public Resource getIcon() {
        String resourceId;
        if (item.iconResourceCode().isEmpty()) {
            resourceId = item.iconResource();
        } else {
            resourceId = i18n.get(item.iconResourceCode());
        }
        if (resourceId.isEmpty()) {
            return null;
        } else {
            return new ThemeResource(resourceId);
        }
    }

    /**
     * @return
     */
    public int getOrder() {
        return item.order();
    }

    /**
     * @param section
     * @return
     */
    public boolean isMemberOfSection(SideBarSectionDescriptor section) {
        return item.sectionId().equals(section.getId());
    }

    @Override
    public int compareTo(SideBarItemDescriptor o) {
        return getOrder() - o.getOrder();
    }

    /**
     * @param ui
     */
    public abstract void itemInvoked(UI ui);

    /**
     *
     */
    public static class ActionItemDescriptor extends SideBarItemDescriptor {

        private final String beanName;
        private final ApplicationContext applicationContext;

        /**
         * @param item
         * @param i18n
         * @param beanName
         * @param applicationContext
         */
        public ActionItemDescriptor(SideBarItem item, I18N i18n, String beanName, ApplicationContext applicationContext) {
            super(item, i18n);
            this.beanName = beanName;
            this.applicationContext = applicationContext;
        }

        @Override
        public void itemInvoked(UI ui) {
            final Runnable operation = applicationContext.getBean(beanName, Runnable.class);
            operation.run();
        }
    }

    /**
     *
     */
    public static class ViewItemDescriptor extends SideBarItemDescriptor {

        private final VaadinView vaadinView;

        /**
         * @param item
         * @param i18n
         * @param vaadinView
         */
        public ViewItemDescriptor(SideBarItem item, I18N i18n, VaadinView vaadinView) {
            super(item, i18n);
            this.vaadinView = vaadinView;
        }

        /**
         * @return
         */
        public String getViewName() {
            return vaadinView.name();
        }

        @Override
        public void itemInvoked(UI ui) {
            ui.getNavigator().navigateTo(vaadinView.name());
        }
    }
}
