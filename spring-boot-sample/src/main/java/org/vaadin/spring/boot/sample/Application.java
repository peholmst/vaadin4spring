/*
 * Copyright 2014 The original authors
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
package org.vaadin.spring.boot.sample;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.VaadinUI;

/**
 * Entry point into the Vaadin web application. You may run this from
 * {@code public static void main} or change the Maven {@code packaging} to {@code war}
 * and deploy to any Servlet 3 container, Java code unchanged.
 *
 * @author Petter Holmström (petter@vaadin.com)
 * @author Josh Long (josh@joshlong.com)
 */
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

@VaadinUI
@Theme("sample")
class RootUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Root UI");
        setContent(new Label("Hello! I'm the root UI!"));
    }
}

@VaadinUI(path = "/anotherUI")
class AnotherUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Another UI");
        setContent(new Label("Hello! I'm a different UI mapped at a different URL!"));
    }
}