package org.vaadin.spring.navigator;

import com.vaadin.ui.UI;
import org.vaadin.spring.VaadinComponent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on {@link com.vaadin.navigator.View}-classes that should be
 * handled by the {@link SpringViewProvider}.
 * <p/>
 * This annotation is also a stereotype annotation, so Spring will automatically detect the annotated classes.
 * <b>However, the scope must be explicitly specified as the default singleton scope will not work!</b> You can use
 * the {@code prototype} scope or the {@link org.vaadin.spring.UIScope ui} scope.
 * <p/>
 * This is an example of a view that is mapped to an empty view name and is available for all UI subclasses in the application:
 * <code>
 *     <pre>
 *     &#64;VaadinView(name = "")
 *     &#64;UIScope
 *     public class MyDefaultView extends CustomComponent implements View {
 *         // ...
 *     }
 *     </pre>
 * </code>
 * This is an example of a view that is only available to a specified UI subclass:
 * <code>
 *     <pre>
 *     &#64;VaadinView(name = "myView", ui = MyUI.class)
 *     &#64;UIScope
 *     public class MyView extends CustomComponent implements View {
 *         // ...
 *     }
 *     </pre>
 * </code>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@VaadinComponent
public @interface VaadinView {

    /**
     * The name of the view. This is the name that is to be passed to the
     * {@link com.vaadin.navigator.Navigator} when navigating to the view. There can be multiple views
     * with the same name as long as they belong to separate UI subclasses.
     *
     * @see #ui()
     */
    String name();

    /**
     * By default, the view will be available for all UI subclasses in the application. This attribute can be used
     * to explicitly specify which subclass (or subclasses) that the view belongs to.
     */
    Class<? extends UI>[] ui() default {};
}
