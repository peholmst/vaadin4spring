package org.vaadin.spring.samples.mvp.security.config;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configures variants of {@link HttpSecurity} influenced by
 * <code>app.security.scheme</code> found in <code>application.yml</code>.
 * Consult <a href="http://docs.spring.io/autorepo/docs/spring-security/4.0.0.CI-SNAPSHOT/reference/htmlsingle/#multiple-httpsecurity">Multiple Security</a>.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
enum HttpSecurityConfigurer {

    INSTANCE;

    void configure(Environment env, ApplicationContext context, HttpSecurity http) throws Exception {
        // all requests are authenticated
        http
        .authorizeRequests().anyRequest().authenticated();

        // have UI peacefully coexist with Apache CXF web-services
        String id = env.getProperty("app.security.scheme", Scheme.BASIC.id());
        Scheme scheme = Scheme.fromValue(id);
        switch(scheme) {
            case FORM:
                http
                .formLogin().failureUrl("/login?error")
                .defaultSuccessUrl("/ui")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .permitAll();
                break;
            case BASIC:
                http.httpBasic();
                break;
            case DIGEST:
                // @see http://java.dzone.com/articles/basic-and-digest
                http.httpBasic();
                http.addFilterAfter(context.getBean(DigestAuthenticationFilter.class), BasicAuthenticationFilter.class);
                break;
        }

        // Vaadin chokes if this filter is enabled, disable it!
        http.csrf().disable();

        // TODO plumb custom HTTP 403 and 404 pages
        /* http.exceptionHandling().accessDeniedPage("/access?error"); */
    }
}
