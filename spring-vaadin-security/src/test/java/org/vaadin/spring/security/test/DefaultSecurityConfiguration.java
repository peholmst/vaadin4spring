package org.vaadin.spring.security.test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.security.annotation.EnableVaadinSecurity;
import org.vaadin.spring.security.config.VaadinSecurityConfiguration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Configuration
@EnableVaadinSecurity
public class DefaultSecurityConfiguration {

    @Bean(name = VaadinSecurityConfiguration.Beans.AUTHENTICATION_MANAGER)
    public AuthenticationManager createAuthenticationManager() {
        AuthenticationManager am = mock(AuthenticationManager.class);
        when(am.authenticate(any(Authentication.class))).thenAnswer(new Answer<Authentication>() {
            public Authentication answer(InvocationOnMock invocation) throws Throwable {
                return (Authentication) invocation.getArguments()[0];
            }
        });

        return am;
    }
    
}
