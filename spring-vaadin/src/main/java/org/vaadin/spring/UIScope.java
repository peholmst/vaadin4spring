package org.vaadin.spring;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

/**
 * Stereotype annotation for Spring's {@code @Scope("ui")}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 */
@Scope("ui")
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UIScope {
}
