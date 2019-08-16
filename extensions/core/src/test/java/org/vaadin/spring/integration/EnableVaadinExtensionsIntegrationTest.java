package org.vaadin.spring.integration;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.vaadin.spring.annotation.EnableVaadinExtensions;
import org.vaadin.spring.context.VaadinApplicationContext;
import org.vaadin.spring.context.VaadinServiceFactory;
import org.vaadin.spring.context.VaadinSessionFactory;
import org.vaadin.spring.http.HttpResponseFactory;
import org.vaadin.spring.http.HttpResponseFilter;
import org.vaadin.spring.http.HttpService;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for the @{@link org.vaadin.spring.annotation.EnableVaadinExtensions} annotation
 *
 * @author erik@vaadin.com
 * @since 15/05/2019
 */
@ExtendWith(SpringExtension.class)
public class EnableVaadinExtensionsIntegrationTest {
    
    private final Class<?>[] BEANS = {
        VaadinSessionFactory.class,
        VaadinServiceFactory.class,
        HttpService.class,
        HttpResponseFactory.class,
        HttpResponseFilter.class,
        RequestContextListener.class,
        VaadinApplicationContext.class
    };

    @Test
    public void testAddingAnnotationEnablesExtensionsAndBeansAreDefined() {
        ApplicationContext applicationContext = getApplicationContext(true);
        Arrays.stream(BEANS).forEach(applicationContext::getBean);
    }

    @Test
    public void testNotAddingAnnotationDoesNotEnableExtensionsAndBeansAreNotDefined() {
        ApplicationContext applicationContext = getApplicationContext(false);
        Arrays.stream(BEANS).forEach(bean ->
                assertThrows(NoSuchBeanDefinitionException.class,
                        () -> applicationContext.getBean(bean)));
    }

    private ApplicationContext getApplicationContext(boolean withAnnotation) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(withAnnotation ? ConfigWithAnnotation.class : ConfigWithoutAnnotation.class);
        context.refresh();
        return context;
    }

    @EnableVaadinExtensions
    public static class ConfigWithAnnotation {}

    @Configuration
    public static class ConfigWithoutAnnotation {}
}
