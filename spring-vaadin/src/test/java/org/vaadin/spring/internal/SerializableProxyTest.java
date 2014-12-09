package org.vaadin.spring.internal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by petterwork on 09/12/14.
 */
public class SerializableProxyTest {

    static interface ServiceInterface {
        String myMethod();
    }

    static class ServiceBean implements ServiceInterface {

        String result = "hello world";

        @Override
        public String myMethod() {
            return result;
        }
    }

    static class SerializableBean implements Serializable {

        final ServiceInterface service;

        @Autowired
        SerializableBean(ServiceInterface service) {
            this.service = service;
        }
    }

    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ServiceBean.class);
        applicationContext.register(SerializableBean.class);
        applicationContext.addBeanFactoryPostProcessor(new ProxyAutowireCandidateResolverConfigurer());
        applicationContext.refresh();
    }

    @Test
    public void constructorParameterInjection_cleanSerializableInstanceRetrievedFromAppContext_serializableProxyHasBeenInjected() {
        SerializableBean bean = applicationContext.getBean(SerializableBean.class);
        ServiceInterface service = bean.service;
        assertTrue(service instanceof Serializable);
        assertEquals("hello world", service.myMethod());
    }
}
