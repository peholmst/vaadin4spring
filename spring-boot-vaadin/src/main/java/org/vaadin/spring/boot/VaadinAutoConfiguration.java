package org.vaadin.spring.boot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.internal.EnableVaadin;
import org.vaadin.spring.internal.SpringAwareVaadinServlet;
import org.vaadin.spring.internal.VaadinUIScope;

/**
 * TODO support the *Configurer pattern to permit parameterization of things \
 * TODO like the {@link org.vaadin.spring.internal.SpringAwareVaadinServlet registration}.
 *
 * @author petter@vaadin.com
 * @author Josh Long (josh@joshlong.com)
 * @see org.vaadin.spring.internal.EnableVaadin
 */
@Configuration
@ConditionalOnClass(VaadinUIScope.class)
public class VaadinAutoConfiguration {

    private static Log logger = LogFactory.getLog(VaadinAutoConfiguration.class);

    /*
     * If the outer {@code \@Configuration} class is enabled (e.g., the
     * {@link VaadinUiScope UI scope} implementation is on the CLASSPATH),
     * _then_ we let Spring import the configuration class.
     */
    @Configuration
    @EnableVaadin
    static class EnableVaadinConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug(getClass().getName() + " has finished running");
        }

        @Bean
        ServletRegistrationBean vaadinServlet() {
            logger.debug("registering vaadinServlet()");

            // TODO Must be possible to parameterize servlet URL mappings and init parameters

            ServletRegistrationBean registrationBean = new ServletRegistrationBean(
                    new SpringAwareVaadinServlet(), "/*", "/VAADIN/*");
            registrationBean.addInitParameter("heartbeatInterval", "10"); // In order to test that orphaned UIs are detached properly
            return registrationBean;
        }
    }
}
