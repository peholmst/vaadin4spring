package org.vaadin.spring.samples.mvp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.Application;
import org.vaadin.spring.test.VaadinAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@VaadinAppConfiguration
public class ApplicationTests {

    @Test
    public void contextLoads() {
    }

}
