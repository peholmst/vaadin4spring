package org.vaadin.spring.security.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityContext;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test Security Bean Injection
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@VaadinAppConfiguration
@ContextConfiguration(classes = {DefaultTestConfiguration.class, DefaultSecurityConfiguration.class, DependecyInjectionTest.DependencyConfig.class})
public class DependecyInjectionTest {
    
    @Autowired
    ApplicationContext applicationContext;
    
    @Autowired
    VaadinSecurity vaadinSecurity;
    
    @Autowired
    VaadinSecurityContext vaadinSecurityContext;
    
    @Autowired
    SecurityAwareClass securityAwareClass;
    
    @Test
    public void testSecurityAutowiring() {
        assertNotNull(vaadinSecurity);
        assertNotNull(vaadinSecurityContext);
        assertNotNull(securityAwareClass);
        assertNotNull(securityAwareClass.getVaadinSecurity());
        assertNotNull(securityAwareClass.getVaadinSecurityContext());
    }
    
    @Test
    public void testSecurityAware() {
        VaadinSecurity vaadinSecurity = securityAwareClass.getVaadinSecurity();
        assertEquals(applicationContext, vaadinSecurity.getApplicationContext());
    }
    
    @Configuration
    public static class DependencyConfig {
        
        @Bean
        SecurityAwareClass securityAware() {
            return new SecurityAwareClass();
        }
        
    }

}
