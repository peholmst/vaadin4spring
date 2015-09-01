package org.vaadin.spring.security.config.util;

import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.vaadin.spring.security.internal.VaadinSharedSecurity;
import org.vaadin.spring.security.web.authentication.AbstractVaadinAuthenticationTargetUrlRequestHandler;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;

/**
 * Created by petterwork on 13/08/15.
 */
public abstract class VaadinSharedSecurityConfigurationHelper extends WebSecurityConfigurerAdapter implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinSharedSecurityConfigurationHelper.class);
    private String loginUrl = "/login";
    private String mainUrl = "/";
    private String serviceUrlPath = "/vaadinServlet";
    private String logoutUrl = "/logout";

    private ApplicationContext applicationContext;

    /**
     *
     */
    protected abstract void setUp();

    @Override
    public void configure(WebSecurity builder) throws Exception {
        LOGGER.info("Setting up WebSecurity for Vaadin shared security");
        builder.ignoring().antMatchers("/VAADIN/**");
    }

    /**
     * @param loginUrl
     * @return
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * @param loginUI
     * @return
     */
    public void setLoginUI(Class<? extends UI> loginUI) {
        SpringUI springUI = loginUI.getAnnotation(SpringUI.class);
        if (springUI == null) {
            throw new IllegalArgumentException("The SpringUI annotation is missing from the login UI class");
        }
        setLoginUrl(springUI.path());
    }

    /**
     * @param mainUrl
     * @return
     */
    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    /**
     * @param mainUI
     * @return
     */
    public void setMainUI(Class<? extends UI> mainUI) {
        SpringUI springUI = mainUI.getAnnotation(SpringUI.class);
        if (springUI == null) {
            throw new IllegalArgumentException("The SpringUI annotation is missing from the main UI class");
        }
        setMainUrl(springUI.path());
    }

    /**
     * @see com.vaadin.spring.server.SpringVaadinServlet#getServiceUrlPath()
     */
    public void setServiceUrlPath(String serviceUrlPath) {
        this.serviceUrlPath = serviceUrlPath;
    }

    @Override
    protected void configure(HttpSecurity builder) throws Exception {
        LOGGER.info("Setting up HttpSecurity for Vaadin shared security");
        builder.csrf().disable(); // Use Vaadin's built-in CSRF protection instead
        builder.authorizeRequests()
                .antMatchers(loginUrl + "/**").anonymous()
                .antMatchers(serviceUrlPath + "/UIDL/**").permitAll()
                .antMatchers(serviceUrlPath + "/HEARTBEAT/**").permitAll()
                .anyRequest().authenticated();
        builder.httpBasic().disable();
        builder.formLogin().disable();
        builder.logout()
                .logoutUrl(logoutUrl)
                .logoutSuccessUrl(loginUrl + "/?logout")
                .permitAll();
        builder.exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginUrl));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            VaadinSharedSecurity sharedSecurity = applicationContext.getBean(VaadinSharedSecurity.class);
            VaadinAuthenticationSuccessHandler authenticationSuccessHandler = sharedSecurity.getVaadinAuthenticationSuccessHandler();
            LOGGER.info("Trying to set up authentication success handler {}", authenticationSuccessHandler);
            if (authenticationSuccessHandler instanceof AbstractVaadinAuthenticationTargetUrlRequestHandler) {
                ((AbstractVaadinAuthenticationTargetUrlRequestHandler) authenticationSuccessHandler).setDefaultTargetUrl(mainUrl);
            } else {
                LOGGER.warn("Unknown authentication success handler, could not set it up");
            }
        } catch (NoSuchBeanDefinitionException ex) {
            LOGGER.info("Shared security is not being used, nothing to configure.");
        }
    }


    /**
     * The authentication manager must be available as a bean so that {@link org.vaadin.spring.security.VaadinSecurity} can use it.
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
