package org.vaadin.spring.samples.mvp.security.config;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.vaadin.spring.samples.mvp.Profiles;


/**
 * The default Security configuration for shared services.
 * Infrastructure is based on Spring Security for declaration of
 * authentication and authorization strategies.
 * Particularly, rely on a "pluggable" authentication/authorization store.
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//@see http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-method
	//@see http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security

	@Inject
	Environment env;

	@Inject
	ApplicationContext context;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		HttpSecurityConfigurer.INSTANCE.configure(env, context, http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (env.acceptsProfiles(Profiles.DEV)) {
			InMemoryAuthenticationBuilder.INSTANCE.build(env, auth);
		} else {
			// FIXME replace with alternate store
			InMemoryAuthenticationBuilder.INSTANCE.build(env, auth);
		}
	}

}
