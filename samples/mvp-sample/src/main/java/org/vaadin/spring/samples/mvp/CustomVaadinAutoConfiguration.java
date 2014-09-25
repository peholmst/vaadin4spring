package org.vaadin.spring.samples.mvp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.boot.config.StaticContentVaadinServletConfiguration;


/**
 * A slightly modified version of {@link org.vaadin.spring.boot.VaadinAutoConfiguration}.
 * Removes <code>EnableVaadinServletConfiguration</code> bean definition.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 * @see org.vaadin.spring.EnableVaadin
 */
@Configuration
@ConditionalOnClass(VaadinUI.class)
public class CustomVaadinAutoConfiguration {

	private static Logger logger = LoggerFactory.getLogger(CustomVaadinAutoConfiguration.class);

	@Configuration
	@EnableVaadin
	@Import(StaticContentVaadinServletConfiguration.class)
	static class EnableVaadinConfiguration implements InitializingBean {
		@Override
		public void afterPropertiesSet() throws Exception {
			logger.debug("{} initialized", getClass().getName());
		}
	}

}
