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
import com.vaadin.ui.UI;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.navigator.VaadinView;

import java.lang.annotation.Annotation;

/**
 * This is a class that describes a side bar item that has been declared using a {@link org.vaadin.spring.stuff.sidebar.SideBarItem} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class SideBarItemDescriptor implements Comparable<SideBarItemDescriptor> {

    private final SideBarItem item;
    private final I18N i18n;
    private final ApplicationContext applicationContext;
    private final String beanName;
    private final Annotation iconAnnotation;
    private final SideBarItemIconProvider<Annotation> iconProvider;

    protected SideBarItemDescriptor(String beanName, ApplicationContext applicationContext) {
        this.item = applicationContext.findAnnotationOnBean(beanName, SideBarItem.class);
        this.i18n = applicationContext.getBean(I18N.class);
        this.applicationContext = applicationContext;
        this.beanName = beanName;
        this.iconAnnotation = findIconAnnotation();
        this.iconProvider = findIconProvider();
    }

    private Annotation findIconAnnotation() {
        Class<?> type = applicationContext.getType(beanName);
        while (type != null) {
            Annotation[] annotations = type.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().isAnnotationPresent(SideBarItemIcon.class)) {
                    return annotation;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private SideBarItemIconProvider<Annotation> findIconProvider() {
        if (iconAnnotation != null) {
            final Class<? extends SideBarItemIconProvider> iconProviderClass = iconAnnotation.annotationType().getAnnotation(SideBarItemIcon.class).value();
            return applicationContext.getBean(iconProviderClass);
        } else {
            return null;
        }
    }

    /**
     * TODO Document me
     *
     * @return
     */
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * TODO Document me
     *
     * @return
     */
    protected String getBeanName() {
        return beanName;
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
     * Returns the icon of the side bar item.
     *
     * @return an icon resource, or {@code null} if the item has no icon.
     */
    public Resource getIcon() {
        if (iconProvider != null) {
            return iconProvider.getIcon(iconAnnotation);
        } else {
            return null;
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

        /**
         * You should never need to create instances of this class directly.
         *
         * @param beanName           the name of the bean that implements the {@link Runnable} to run, must not be {@code null}.
         * @param applicationContext the application context to use when looking up beans, must not be {@code null}.
         */
        public ActionItemDescriptor(String beanName, ApplicationContext applicationContext) {
            super(beanName, applicationContext);
        }

        @Override
        public void itemInvoked(UI ui) {
            final Runnable operation = getApplicationContext().getBean(getBeanName(), Runnable.class);
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
         * @param beanName           the name of the bean that implements the view to go to, must not be {@code null}.
         * @param applicationContext the application context to use when looking up beans, must not be {@code null}.
         */
        public ViewItemDescriptor(String beanName, ApplicationContext applicationContext) {
            super(beanName, applicationContext);
            this.vaadinView = applicationContext.findAnnotationOnBean(beanName, VaadinView.class);
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
