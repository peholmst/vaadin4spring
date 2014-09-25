package org.vaadin.spring.samples.mvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vaadin.spring.EnableVaadin;
import org.vaadin.spring.boot.VaadinAutoConfiguration;
import org.vaadin.spring.boot.config.CustomVaadinServletConfiguration;

/**
 * Bootstraps application using Spring Boot API Indiscriminately and recursively
 * scans for beans on classpath
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude={VaadinAutoConfiguration.class})
@Import(CustomVaadinServletConfiguration.class)
@EnableVaadin
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

