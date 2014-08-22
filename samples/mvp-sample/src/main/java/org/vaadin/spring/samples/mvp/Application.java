package org.vaadin.spring.samples.mvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.samples.mvp.ui.mock.MockUserService;
import org.vaadin.spring.samples.mvp.ui.service.UserService;

/**
 * Bootstraps application using Spring Boot API Indiscriminately and recursively
 * scans for beans on classpath
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

    private static Class<Application> entryPointClass = Application.class;

    public static void main(String[] args) {
        SpringApplication.run(entryPointClass, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(entryPointClass);
    }

    @Bean
    UserService userService() {
        return new MockUserService();
    }
}
