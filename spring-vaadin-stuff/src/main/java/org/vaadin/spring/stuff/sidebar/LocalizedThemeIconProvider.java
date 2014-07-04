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
import org.vaadin.spring.i18n.I18N;

/**
 * Icon provider for {@link org.vaadin.spring.stuff.sidebar.LocalizedThemeIcon}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class LocalizedThemeIconProvider implements SideBarItemIconProvider<LocalizedThemeIcon> {

    private final I18N i18n;

    public LocalizedThemeIconProvider(I18N i18n) {
        this.i18n = i18n;
    }

    @Override
    public Resource getIcon(LocalizedThemeIcon annotation) {
        final String resourceId = i18n.get(annotation.value());
        return new ThemeResource(resourceId);
    }
}
