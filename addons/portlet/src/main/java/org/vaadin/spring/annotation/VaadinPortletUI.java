package org.vaadin.spring.annotation;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be put on {@link com.vaadin.ui.UI}-subclasses that are to be automatically detected and configured
 * by Spring. Use it like this:
 * <pre>
 *     &#64;VaadinPortletUI
 *     public class MyRootPortletUI extends UI {
 *         // ...
 *     }
 * </pre>
 *
 * The annotated UI will automatically be placed in the {@link com.vaadin.spring.annotation.UIScope},
 * so there is no need to add that annotation explicitly.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringComponent
@UIScope
public @interface VaadinPortletUI {
}
