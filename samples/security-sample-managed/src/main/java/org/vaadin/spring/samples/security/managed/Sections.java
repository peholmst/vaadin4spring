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
package org.vaadin.spring.samples.security.managed;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

/**
 * Component that is only used to declare the sections of the side bar.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Component
@SideBarSections({
        @SideBarSection(id = Sections.VIEWS, caption = "Views"),
        @SideBarSection(id = Sections.OPERATIONS, caption = "Operations")
})
public class Sections {

    public static final String VIEWS = "views";
    public static final String OPERATIONS = "operations";
}
