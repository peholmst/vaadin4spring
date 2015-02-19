/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.events.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * Test Security Bean Injection
 * 
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@VaadinAppConfiguration
@ContextConfiguration(classes = {DefaultTestConfiguration.class, DependecyInjectionTest.DependencyConfig.class})
public class DependecyInjectionTest {
    
    @Autowired
    ApplicationContext applicationContext;
    
    @Autowired
    EventBus.ApplicationEventBus eventBus;
    
    @Autowired
    EventBusAwareClass eventBusAwareClass;
    
    @Test
    public void testSecurityAutowiring() {
        assertNotNull(eventBus);
        assertNotNull(eventBusAwareClass);
        assertNotNull(eventBusAwareClass.getEventBus());
    }
    
    @Test
    public void testSecurityAware() {
        EventBus eventBus = eventBusAwareClass.getEventBus();
        assertNotNull(eventBus);
    }
    
    @Configuration
    public static class DependencyConfig {
        
        @Bean
        EventBusAwareClass eventBusAware() {
            return new EventBusAwareClass();
        }
        
    }

}
