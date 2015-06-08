package org.vaadin.spring.samples.security.single;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.vaadin.spring.security.annotation.EnableVaadinSecurity;

/**
 * Created by petterwork on 05/06/15.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @EnableWebSecurity
    static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("user").password("user").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable(); // Use Vaadin's CSRF support
            http.authorizeRequests().anyRequest().permitAll(); // Permit access to the entire application; it will handle security internally.
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/VAADIN/**"); // Ignore static resources
        }
    }
}
