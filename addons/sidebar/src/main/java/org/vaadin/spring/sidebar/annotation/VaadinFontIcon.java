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
package org.vaadin.spring.sidebar.annotation;

import com.vaadin.icons.VaadinIcons;
import org.vaadin.spring.sidebar.VaadinFontIconProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is placed next to {@link org.vaadin.spring.sidebar.annotation.SideBarItem}, instructing the side bar
 * to use a {@link org.vaadin.teemu.VaadinIcons Vaadin Font Icon} for the item.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SideBarItemIcon(VaadinFontIconProvider.class)
public @interface VaadinFontIcon {

    /**
     * The item icon.
     */
    VaadinIcons value();
}
