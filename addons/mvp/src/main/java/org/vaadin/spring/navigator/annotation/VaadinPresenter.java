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
package org.vaadin.spring.navigator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

/**
 * Annotation to be placed on {@link org.vaadin.spring.navigator.Presenter}-classes that employ a
 * {@link com.vaadin.spring.navigator.SpringViewProvider} and an {@link org.vaadin.spring.events.EventBus}.
 * <p>
 * This annotation is also a stereotype annotation, so Spring will automatically detect the annotated classes.
 * <p>
 * This is an example of a presenter that is mapped to a view name:
 * <pre>
 * <code>
 *     &#64;VaadinPresenter(viewName = "myView")
 *     public class MyPresenter extends Presenter {
 *         // ...
 *     }
 * </code>
 * </pre>
 *
 * The <code>viewName</code> must match the <code>name</code> attribute of a {@link com.vaadin.spring.annotation.SpringView} annotated {@link com.vaadin.navigator.View}.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@UIScope
@SpringComponent
public @interface VaadinPresenter {

    /**
     * A presenter will always be matched with a view in an application.
     */
    String viewName();
}
