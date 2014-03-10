package org.vaadin.spring.boot.config;

import com.vaadin.annotations.VaadinServletConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

/**
 * @author petter@vaadin.com
 */
abstract class AbstractServletConfiguration {

    protected static Logger logger = LoggerFactory.getLogger(AbstractServletConfiguration.class);

    protected abstract Environment getEnvironment();

    protected abstract String getServletConfigurationParameterPrefix();

    protected void addInitParameters(ServletRegistrationBean servletRegistrationBean) {
        logger.debug("Looking for servlet init parameters");
        final Method[] methods = VaadinServletConfiguration.class
                .getDeclaredMethods();
        for (Method method : methods) {
            VaadinServletConfiguration.InitParameterName name = method
                    .getAnnotation(VaadinServletConfiguration.InitParameterName.class);
            String propertyValue = getEnvironment().getProperty(getServletConfigurationParameterPrefix() + name.value());
            if (propertyValue != null) {
                logger.debug(String.format("Found servlet init parameter [%s] = [%s]", name.value(), propertyValue));
                servletRegistrationBean.addInitParameter(name.value(), propertyValue);
            }
        }
    }
}
