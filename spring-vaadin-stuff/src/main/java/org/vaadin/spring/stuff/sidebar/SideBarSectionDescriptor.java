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
 * TODO Document me!
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SideBarSectionDescriptor implements Comparable<SideBarSectionDescriptor> {

    private final SideBarSection section;
    private final I18N i18n;
    private final Set<Class<? extends UI>> availableUIClasses;

    /**
     * @param section
     * @param i18n
     */
    public SideBarSectionDescriptor(SideBarSection section, I18N i18n) {
        this.section = section;
        this.i18n = i18n;
        availableUIClasses = new HashSet<>(Arrays.asList(section.ui()));
    }

    /**
     * @return
     */
    public String getCaption() {
        if (section.captionCode().isEmpty()) {
            return section.caption();
        } else {
            return i18n.get(section.captionCode());
        }
    }

    /**
     * @return
     */
    public int getOrder() {
        return section.order();
    }

    /**
     * @return
     */
    public String getId() {
        return section.id();
    }

    /**
     * @param uiClass
     * @return
     */
    public boolean isAvailableFor(Class<? extends UI> uiClass) {
        return availableUIClasses.isEmpty() || availableUIClasses.contains(uiClass);
    }

    @Override
    public int compareTo(SideBarSectionDescriptor o) {
        return getOrder() - o.getOrder();
    }
}
