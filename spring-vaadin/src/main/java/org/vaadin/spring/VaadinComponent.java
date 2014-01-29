package org.vaadin.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Alias for {@link org.springframework.stereotype.Component} to prevent conflicts with {@link com.vaadin.ui.Component}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface VaadinComponent {
    String value() default "";
}