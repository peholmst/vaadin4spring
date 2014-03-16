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
 * TODO Document me!
 * <p/>
 * This annotation can be placed on two types of beans:
 * <ol>
 * <li>{@link java.lang.Runnable}s - when the item is clicked, the runnable is executed</li>
 * <li>{@link com.vaadin.navigator.View Views}s that are also annotated with {@link org.vaadin.spring.navigator.VaadinView VaadinView} - when the item is clicked, the {@link com.vaadin.navigator.Navigator navigator} navigates to the view.
 * </ol>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SideBarItem {

    /**
     * @return
     */
    String sectionId();

    /**
     * @return
     */
    String caption() default "";

    /**
     * @return
     */
    String captionCode() default "";

    /**
     * @return
     */
    String iconResource() default "";

    /**
     * @return
     */
    String iconResourceCode() default "";

    /**
     * @return
     */
    int order() default Integer.MAX_VALUE;
}
