package org.vaadin.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * TODO Document me!
 *
 * @author petter@vaadin.com
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@VaadinComponent
@UIScope
public @interface VaadinUI {

    /**
     * @return
     */
    String path() default "";
}
