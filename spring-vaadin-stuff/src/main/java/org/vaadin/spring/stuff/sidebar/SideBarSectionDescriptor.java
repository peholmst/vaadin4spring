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

import com.vaadin.ui.UI;
import org.vaadin.spring.i18n.I18N;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a class that describes a side bar section that has been declared using a {@link org.vaadin.spring.stuff.sidebar.SideBarSection} annotation.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SideBarSectionDescriptor implements Comparable<SideBarSectionDescriptor> {

    private final SideBarSection section;
    private final I18N i18n;
    private final Set<Class<? extends UI>> availableUIClasses;

    /**
     * You should never need to create instances of this class directly.
     *
     * @param section the annotation, must not be {@code null}.
     * @param i18n    the {@link org.vaadin.spring.i18n.I18N} instance to use when looking up localized captions, must not be {@code null}.
     */
    public SideBarSectionDescriptor(SideBarSection section, I18N i18n) {
        this.section = section;
        this.i18n = i18n;
        availableUIClasses = new HashSet<>(Arrays.asList(section.ui()));
    }

    /**
     * Returns the caption of this side bar section. If the caption was specified using {@link org.vaadin.spring.stuff.sidebar.SideBarSection#captionCode()},
     * this method will fetch the string from {@link org.vaadin.spring.i18n.I18N}.
     *
     * @return a string, never {@code null}.
     */
    public String getCaption() {
        if (section.captionCode().isEmpty()) {
            return section.caption();
        } else {
            return i18n.get(section.captionCode());
        }
    }

    /**
     * Returns the order of the side bar section within the side bar.
     */
    public int getOrder() {
        return section.order();
    }

    /**
     * Returns the ID of this side bar section.
     *
     * @return a string, never {@code null}.
     */
    public String getId() {
        return section.id();
    }

    /**
     * Checks if this section is available for the specified UI subclass.
     *
     * @param uiClass the UI subclass, must not be {@code null}.
     * @return true if the section is available, false otherwise.
     */
    public boolean isAvailableFor(Class<? extends UI> uiClass) {
        return availableUIClasses.isEmpty() || availableUIClasses.contains(uiClass);
    }

    @Override
    public int compareTo(SideBarSectionDescriptor o) {
        return getOrder() - o.getOrder();
    }
}
