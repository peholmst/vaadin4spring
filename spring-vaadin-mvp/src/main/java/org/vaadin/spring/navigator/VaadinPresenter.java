package org.vaadin.spring.navigator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;

/**
 * Annotation to be placed on {@link org.vaadin.spring.navigator.Presenter}-classes that employ a
 * {@link SpringViewProvider} and an {@link org.vaadin.spring.events.EventBus}.
 * <p/>
 * This annotation is also a stereotype annotation, so Spring will automatically detect the annotated classes.
 * <p/>
 * This is an example of a presenter that is mapped to a view name:
 * <code>
 *     <pre>
 *     &#64;VaadinPresenter(viewName = "myView")
 *     public class MyPresenter extends Presenter {
 *         // ...
 *     }
 *     </pre>
 * </code>
 *
 * The <code>viewName</code> must match the <code>name</code> attribute of a {@link org.vaadin.spring.navigator.VaadinView} annotated {@link com.vaadin.navigator.View}.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@UIScope
@VaadinComponent
public @interface VaadinPresenter {

    /**
     * A presenter will always be matched with a view in an application.
     */
    String viewName();
}
