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
package org.vaadin.spring.samples.sidebar;

import org.springframework.stereotype.Component;
import org.vaadin.spring.stuff.sidebar.SideBarSection;
import org.vaadin.spring.stuff.sidebar.SideBarSections;

/**
 * This is a Spring-managed bean that does not do anything. Its only purpose is to define
 * the sections of the side bar. The reason it is configured as a bean is that it makes it possible
 * to lookup the annotations from the Spring application context.
 */
@SideBarSections({
        @SideBarSection(id = Sections.PLANNING, caption = "Planning"),
        @SideBarSection(id = Sections.EXECUTION, caption = "Execution"),
        @SideBarSection(id = Sections.REPORTING, caption = "Reporting")
})
@Component
public class Sections {

    public static final String PLANNING = "planning";
    public static final String EXECUTION = "execution";
    public static final String REPORTING = "reporting";
}
