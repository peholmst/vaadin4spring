package org.vaadin.spring.internal;

import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.Serializable;

/**
 * Created by petterwork on 09/12/14.
 */
public class ProxyAutowireCandidateResolver extends SimpleAutowireCandidateResolver {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyAutowireCandidateResolver.class);

    @Override
    public Object getSuggestedValue(DependencyDescriptor dependencyDescriptor) {
        if (isOwningClassSerializable(dependencyDescriptor) && !isSerializable(dependencyDescriptor.getDependencyType())) {
            if (dependencyDescriptor.getDependencyType().isInterface()) {
                LOGGER.debug("Creating serializable proxy of {}", dependencyDescriptor.getDependencyType());
                return makeSerializableProxy(dependencyDescriptor.getDependencyType());
            } else {
                LOGGER.warn("Injecting non-serializable instance into serializable bean ({})", dependencyDescriptor);
            }
        }
        return null;
    }

    private boolean isOwningClassSerializable(DependencyDescriptor dependencyDescriptor) {
        Class<?> owningClass;
        if (dependencyDescriptor.getField() != null) {
            owningClass = dependencyDescriptor.getField().getDeclaringClass();
        } else {
            owningClass = dependencyDescriptor.getMethodParameter().getDeclaringClass();
        }
        return isSerializable(owningClass);
    }

    private boolean isSerializable(Class<?> clazz) {
        return Serializable.class.isAssignableFrom(clazz);
    }

    private Object makeSerializableProxy(Class<?> type) {
        ProxyFactory proxyFactory = new ProxyFactory(type, Serializable.class);
        proxyFactory.addAdvice(new SerializableInterceptor(type));
        return proxyFactory.getProxy();
    }

    private static class SerializableInterceptor implements MethodInterceptor, Serializable {
        private transient Object target;
        private final Class<?> targetType;

        private SerializableInterceptor(Class<?> targetType) {
            this.targetType = targetType;
        }

        private Object getTarget() {
            if (target == null) {
                target = findBeanFactory().getBean(targetType);
            }
            return target;
        }

        private BeanFactory findBeanFactory() {
            return null;
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            return methodInvocation.getMethod().invoke(getTarget(), methodInvocation.getArguments());
        }
    }


}
