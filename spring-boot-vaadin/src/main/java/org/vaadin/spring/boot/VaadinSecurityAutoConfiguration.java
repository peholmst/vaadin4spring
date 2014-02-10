/*
 * Copyright 2014 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.boot;

import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.boot.security.ExternalUIAuthentication;
import org.vaadin.spring.boot.security.SelfContainedAuthentication;
import org.vaadin.spring.security.EnableVaadinSecurity;
import org.vaadin.spring.security.Security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Petter Holmström (petter@vaadin.com)
 * @see org.vaadin.spring.security.EnableVaadinSecurity
 */
@Configuration
@ConditionalOnClass(Security.class)
public class VaadinSecurityAutoConfiguration {

    private static Log logger = LogFactory.getLog(VaadinSecurityAutoConfiguration.class);

    @Configuration
    @EnableVaadinSecurity
    static class EnableVaadinSecurityConfiguration extends WebSecurityConfigurerAdapter implements InitializingBean {

        @Autowired
        ApplicationContext applicationContext;

        @Autowired
        Environment environment;

        private String servletPath;

        @Override
        public void afterPropertiesSet() throws Exception {
            logger.debug(getClass().getName() + " has finished running");
            String servletMapping = environment.getProperty(VaadinAutoConfiguration.EnableVaadinConfiguration.SERVLET_URL_MAPPING_PARAMETER_NAME,
                    VaadinAutoConfiguration.EnableVaadinConfiguration.DEFAULT_SERVLET_URL_MAPPING);

            if (!servletMapping.endsWith("/*")) {
                throw new IllegalStateException(String.format("Invalid servlet mapping [%s]. The mapping must end with [/*]", servletMapping));
            }
            servletPath = servletMapping.substring(0, servletMapping.length() - 2);
        }


        @Override
        public void configure(WebSecurity web) throws Exception {
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            logger.debug("Allowing access to Vaadin static resources");
            http.authorizeRequests().antMatchers("/VAADIN/*").permitAll();

            final Collection<Class<? extends UI>> permittedUIs = new HashSet<>();
            for (Class<? extends UI> uiClass : getUIClasses()) {
                if (uiClass.isAnnotationPresent(SelfContainedAuthentication.class)) {
                    permittedUIs.add(uiClass);
                } else if (uiClass.isAnnotationPresent(ExternalUIAuthentication.class)) {
                    permittedUIs.add(uiClass.getAnnotation(ExternalUIAuthentication.class).authenticationUI());
                    // TODO Forward to authentication UI when authentication is required
                }
            }
            for (Class<? extends UI> permittedUiClass : permittedUIs) {
                final String urlPattern = getUrlPatternOfUI(permittedUiClass);
                logger.debug(String.format("Allowing access to Vaadin UI [%s] mapped to URL [%s]", permittedUiClass.getCanonicalName(), urlPattern));
                http.authorizeRequests().antMatchers(urlPattern).permitAll();
            }
        }

        private String getUrlPatternOfUI(Class<? extends UI> uiClass) {
            return servletPath + uiClass.getAnnotation(VaadinUI.class).path() + "/*";
        }

        private Collection<Class<? extends UI>> getUIClasses() {
            final String[] beanNames = applicationContext.getBeanNamesForType(UI.class);
            final Collection<Class<? extends UI>> beanClasses = new ArrayList<>(beanNames.length);
            for (String beanName : beanNames) {
                Class<? extends UI> uiType = (Class<? extends UI>) applicationContext.getType(beanName);
                if (uiType.isAnnotationPresent(VaadinUI.class)) {
                    beanClasses.add(uiType);
                }
            }
            return beanClasses;
        }
    }
}
