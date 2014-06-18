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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to declare a {@link org.vaadin.spring.stuff.sidebar.SideBar} section that can contain items.
 * It has to be placed on a Spring-managed bean in order to get detected, but the bean can be an empty singleton without methods.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.stuff.sidebar.SideBar
 * @see org.vaadin.spring.stuff.sidebar.SideBarItem
 * @see org.vaadin.spring.stuff.sidebar.SideBarSections
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SideBarSection {

    /**
     * The ID of the side bar section. Must be unique within the application.
     */
    String id();

    /**
     * The caption of this section.
     */
    String caption() default "";

    /**
     * The code to pass to the {@link org.vaadin.spring.i18n.I18N} instance to get the section caption.
     * If this is an empty string, {@link #caption()} is used instead.
     *
     * @see org.vaadin.spring.i18n.I18N#get(String, Object...)
     */
    String captionCode() default "";

    /**
     * The {@link com.vaadin.ui.UI} subclasses that this section is available for. If no classes are specified,
     * the section will be available for all UI subclasses within the application.
     */
    Class<? extends UI>[] ui() default {};

    /**
     * The order of this section within the side bar. Sections with a lower order are placed higher up in the list.
     */
    int order() default Integer.MAX_VALUE;
}
