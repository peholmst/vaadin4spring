package org.vaadin.spring.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * TODO Document me!
 */
public class ProxyAutowireCandidateResolverConfigurer implements BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyAutowireCandidateResolverConfigurer.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
            LOGGER.info("Registering ProxyAutowireCandidateResolver");
            defaultListableBeanFactory.setAutowireCandidateResolver(new ProxyAutowireCandidateResolver());
        } else {
            LOGGER.warn("Bean factory is not an instance of DefaultListableBeanFactory, cannot register ProxyAutowireCandidateResolver");
        }
    }
}
