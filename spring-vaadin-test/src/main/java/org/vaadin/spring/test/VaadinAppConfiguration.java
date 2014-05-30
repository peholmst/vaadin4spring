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
package org.vaadin.spring.test;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Place this annotation on test classes that:
 * <ul>
 * <li>are run with the {@link org.springframework.test.context.junit4.SpringJUnit4ClassRunner},</li>
 * <li>use autowiring to inject managed beans into the actual test, and</li>
 * <li>perform tests on beans that are {@link org.vaadin.spring.UIScope}d</li>
 * </ul>
 * With this annotation in place, all test methods will run inside the context of a {@link org.vaadin.spring.test.MockUI},
 * and all beans that are UI-scoped or session scoped will work as expected. The indented use case for this approach is
 * to test non-visual components like presenters or controllers. It is not usable for testing Vaadin components or
 * actual {@link com.vaadin.ui.UI} instances.
 * <p/>
 * Example of usage:
 * <code>
 * <pre>
 *   &#64;RunWith(SpringJUnit4ClassRunner.class)
 *   &#64;VaadinAppConfiguration
 *   &#64;ContextConfiguration(classes = ExampleIntegrationTest.Config.class)
 *   public class MyTest {
 *
 *       &#64;Autowired MyUIScopedController myController;
 *
 *       ...
 *   }
 *   </pre>
 * </code>
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebAppConfiguration
@TestExecutionListeners({
        ServletTestExecutionListener.class,
        VaadinTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
public @interface VaadinAppConfiguration {
}
