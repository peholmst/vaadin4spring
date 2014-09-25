package org.vaadin.spring.boot.config;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Specialized {@link VaadinServletConfiguration}. Allows for initialization of custom widget sets.
 * Secret sauce mentioned <a href="https://vaadin.com/forum/#!/thread/7476924/7481607">here</a>.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
public class CustomVaadinServletConfiguration extends VaadinServletConfiguration {

	@Override
	@Bean
	ServletRegistrationBean vaadinServletRegistration() {
		ServletRegistrationBean bean = createServletRegistrationBean();
		bean.addInitParameter("widgetset", "org.vaadin.spring.samples.mvp.ui.widgetset.CustomWidgetSet");
		bean.getUrlMappings().add("/VAADIN/*");
		return bean;
	}

}
