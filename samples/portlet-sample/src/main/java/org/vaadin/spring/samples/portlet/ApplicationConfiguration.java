package org.vaadin.spring.samples.portlet;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.config.VaadinConfiguration;

@Configuration
@Import(VaadinConfiguration.class)
@ComponentScan(basePackages = "org.vaadin.spring.samples.portlet.ui")
public class ApplicationConfiguration {
}
