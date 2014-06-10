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
 * This is a class that describes a side bar item that has been declared using a {@link org.vaadin.spring.stuff.sidebar.SideBarItem} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class SideBarItemDescriptor implements Comparable<SideBarItemDescriptor> {

    private final SideBarItem item;
    private final I18N i18n;

    protected SideBarItemDescriptor(SideBarItem item, I18N i18n) {
        this.item = item;
        this.i18n = i18n;
    }

    /**
     * Returns the caption of this side bar item. If the caption was specified using {@link org.vaadin.spring.stuff.sidebar.SideBarItem#captionCode()},
     * this method will fetch the string from {@link org.vaadin.spring.i18n.I18N}.
     *
     * @return a string, never {@code null}.
     */
    public String getCaption() {
        if (item.captionCode().isEmpty()) {
            return item.caption();
        } else {
            return i18n.get(item.captionCode());
        }
    }

    /**
     * Returns the icon of the side bar item. If the resource ID was specified using {@link org.vaadin.spring.stuff.sidebar.SideBarItem#iconResourceCode()},
     * this method will fetch the real resource ID from {@link org.vaadin.spring.i18n.I18N}.
     *
     * @return an icon resource, or {@code null} if the item has no icon.
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
     * Returns the order of this side bar item within the section.
     */
    public int getOrder() {
        return item.order();
    }

    /**
     * Checks if this item is a member of the specified side bar section.
     *
     * @param section the side bar section, must not be {@code null}.
     * @return true if the item is a member, false otherwise.
     */
    public boolean isMemberOfSection(SideBarSectionDescriptor section) {
        return item.sectionId().equals(section.getId());
    }

    @Override
    public int compareTo(SideBarItemDescriptor o) {
        return getOrder() - o.getOrder();
    }

    /**
     * This method must be called when the user clicks the item in the UI.
     *
     * @param ui the UI in which the item was invoked, must not be {@code null}.
     */
    public abstract void itemInvoked(UI ui);

    /**
     * Side bar item descriptor for action items. When invoked, the descriptor will execute the operation.
     */
    public static class ActionItemDescriptor extends SideBarItemDescriptor {

        private final String beanName;
        private final ApplicationContext applicationContext;

        /**
         * You should never need to create instances of this class directly.
         *
         * @param item               the annotation, must not be {@code null}.
         * @param i18n               the {@link org.vaadin.spring.i18n.I18N} instance to use when looking up localized captions and icons, must not be {@code null}.
         * @param beanName           the name of the bean that implements the {@link Runnable} to run, must not be {@code null}.
         * @param applicationContext the application context to use when looking up the runnable bean, must not be {@code null}.
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
     * Side bar item descriptor for view items. When invoked, the descriptor will navigate to the
     * view.
     */
    public static class ViewItemDescriptor extends SideBarItemDescriptor {

        private final VaadinView vaadinView;

        /**
         * You should never need to create instances of this class directly.
         *
         * @param item       the annotation, must not be {@code null}.
         * @param i18n       the {@link org.vaadin.spring.i18n.I18N} instance to use when looking up localized captions and icons, must not be {@code null}.
         * @param vaadinView the view annotation, must not be {@code null}.
         */
        public ViewItemDescriptor(SideBarItem item, I18N i18n, VaadinView vaadinView) {
            super(item, i18n);
            this.vaadinView = vaadinView;
        }

        /**
         * Gets the name of the view to navigate to.
         *
         * @return a string, never {@code null}.
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
