package org.vaadin.spring.context;

import com.vaadin.flow.server.VaadinService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NamedBean;

/**
 * Factory bean that makes the current {@link com.vaadin.flow.server.VaadinService} available
 * for injection. Only works if {@link com.vaadin.flow.server.VaadinService#getCurrent()} does not
 * return {@code null}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinServiceFactory implements FactoryBean<VaadinService>, NamedBean {

    public static final String BEAN_NAME = "vaadinServiceFactory";

    @Override
    public VaadinService getObject() {
        final VaadinService vaadinService = VaadinService.getCurrent();
        if (vaadinService == null) {
            throw new IllegalStateException("No VaadinService bound to current thread");
        }
        return vaadinService;
    }

    @Override
    public Class<?> getObjectType() {
        return VaadinService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }
}
