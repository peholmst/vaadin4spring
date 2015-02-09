package org.vaadin.spring.navigator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.vaadin.spring.navigator.annotation.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

public abstract class AbstractVaadinViewProvider implements ApplicationContextAware, InitializingBean, ViewProvider {

	private static final long serialVersionUID = 8435622290477038507L;

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	protected ApplicationContext applicationContext;
	protected BeanDefinitionRegistry beandefinitionRegistry;
	protected Map<String, Set<String>> vaadinViewMap = new ConcurrentHashMap<String, Set<String>>();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		this.beandefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(applicationContext);
		Assert.notNull(beandefinitionRegistry);
		
		int count = 0;
		for(String beanName : getBeanForAnnotationAssignedFrom(VaadinView.class, View.class)) {
			final VaadinView annotation = applicationContext.findAnnotationOnBean(beanName, VaadinView.class);
			final String viewName = annotation.name();
			log.debug("Found VaadinView bean [{}] with view name [{}]", beanName, viewName);
			if ( applicationContext.isSingleton(beanName) ) {
				throw new IllegalStateException("VaadinView bean [" + beanName + "] cannot be a singleton");
			}
			
			Set<String> beanNames = vaadinViewMap.get(viewName);
			if (beanNames == null) {
				beanNames = new ConcurrentSkipListSet<String>();
				vaadinViewMap.put(viewName, beanNames);
			}
			beanNames.add(beanName);
			count++;
		}
		
		log.info("{} VaadinView(s) found", (count == 0 ? "No" : count));
	}
	
	protected final String[] getBeansForAnnotation(Class<? extends Annotation> clazz) {
		return applicationContext.getBeanNamesForAnnotation(clazz);
	}
	
	protected final String[] getBeanForAnnotationAssignedFrom(Class<? extends Annotation> clazz, Class<?> assignedFromClass) {
		final String[] beanNames = getBeansForAnnotation(clazz);
		List<String> beans = Collections.synchronizedList(new ArrayList<String>());
		for(String beanName : beanNames) {
			final Class<?> type = applicationContext.getType(beanName);
			if ( assignedFromClass.isAssignableFrom(type) ) {
				beans.add(beanName);
			}
		}
		return beans.toArray(new String[]{});
	}
}
