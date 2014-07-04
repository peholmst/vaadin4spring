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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is placed next to {@link SideBarItem}, instructing the side bar
 * to use a {@link com.vaadin.server.ThemeResource} icon for the item, but to look up the actual
 * resource ID from an {@link org.vaadin.spring.i18n.I18N}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SideBarItemIcon(LocalizedThemeIconProvider.class)
public @interface LocalizedThemeIcon {

    /**
     * The key to use when looking up the real resource ID from an {@link org.vaadin.spring.i18n.I18N}.
     */
    String value();
}
