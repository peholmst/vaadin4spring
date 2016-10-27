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
package org.vaadin.spring.boot;

import com.vaadin.server.VaadinServlet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

/**
 * This test tests that the servlet registration is working properly and that Vaadin Spring picks
 * up the correct custom Vaadin Servlet from the application context.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CustomServletOverrideTest.MyConfiguration.class})
@WebAppConfiguration
public class CustomServletOverrideTest {

    public static class MyCustomVaadinServlet extends VaadinServlet {
    }

    @Configuration
    @EnableAutoConfiguration
    public static class MyConfiguration {

        @Bean
        MyCustomVaadinServlet vaadinServlet() {
            return new MyCustomVaadinServlet();
        }
    }

    @Resource(name = "vaadinServletRegistration")
    ServletRegistrationBean servletRegistrationBean;

    @Test
    public void customServletIsInjected() throws Exception {
        Method getServlet = ServletRegistrationBean.class.getDeclaredMethod("getServlet");
        getServlet.setAccessible(true);
        Servlet servlet = (Servlet) getServlet.invoke(servletRegistrationBean);
        assertTrue("expected MyCustomVaadinServlet, was " + servlet.getClass().getSimpleName(), servlet instanceof MyCustomVaadinServlet);
    }
}
