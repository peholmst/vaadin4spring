package org.vaadin.spring.samples.mvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Bootstraps application using Spring Boot API indiscriminately and recursively
 * scans for beans on classpath
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

