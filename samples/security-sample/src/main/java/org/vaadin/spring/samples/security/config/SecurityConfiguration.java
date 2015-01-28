package org.vaadin.spring.samples.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.vaadin.spring.security.annotation.EnableVaadinSecurity;

@Configuration
@ComponentScan
@EnableWebSecurity
public class SecurityConfiguration {

    @Configuration
    @EnableVaadinSecurity
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        
        @Bean(name="authenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
        
        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                .ignoring()
                    .antMatchers("/VAADIN/**", "/UIDL/**", "/HEARTBEAT/**", "/beans/**");
        }
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication()
                    .withUser("user").password("user").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN");
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            
            http
                .authorizeRequests()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/**").authenticated()
                    .anyRequest().authenticated()
                .and()
                /*
                .securityContext()
                    .securityContextRepository(new VaadinSessionSecurityContextRepository())
                .and()
                */
                .sessionManagement()
                    .sessionFixation()
                        .migrateSession()
                .and()
                .csrf().disable()
                .headers()
                    .frameOptions().disable()
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
            
        }
        
    }
    
}
