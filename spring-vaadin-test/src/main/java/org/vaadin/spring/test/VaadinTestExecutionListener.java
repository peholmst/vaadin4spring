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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinTestExecutionListener extends AbstractTestExecutionListener {

    /**
     * Attribute name for a {@link TestContext} attribute which indicates that
     * {@code VaadinTestExecutionListener} has already set the current UI.
     * <p/>
     * <p>Permissible values include {@link Boolean#TRUE} and {@link Boolean#FALSE}.
     */
    public static final String SET_CURRENT_UI_ATTRIBUTE = Conventions.getQualifiedAttributeName(
            VaadinTestExecutionListener.class, "setCurrentUI");
    private static final Logger logger = LoggerFactory.getLogger(VaadinTestExecutionListener.class);

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        setCurrentUIIfNecessary(testContext);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        setCurrentUIIfNecessary(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        clearCurrentUIIfNecessary(testContext);
    }

    private boolean notAnnotatedWithVaadinAppConfiguration(TestContext testContext) {
        return AnnotationUtils.findAnnotation(testContext.getTestClass(), VaadinAppConfiguration.class) == null;
    }

    private boolean alreadySetCurrentUI(TestContext testContext) {
        return Boolean.TRUE.equals(testContext.getAttribute(SET_CURRENT_UI_ATTRIBUTE));
    }

    private synchronized void setCurrentUIIfNecessary(TestContext testContext) {
        if (notAnnotatedWithVaadinAppConfiguration(testContext) || alreadySetCurrentUI(testContext)) {
            logger.debug("No need to set up MockUI for test context [{}]", testContext);
            return;
        }

        final ApplicationContext context = testContext.getApplicationContext();
        MockUI.setUp(context);
        testContext.setAttribute(SET_CURRENT_UI_ATTRIBUTE, Boolean.TRUE);
    }

    private synchronized void clearCurrentUIIfNecessary(TestContext testContext) {
        if (notAnnotatedWithVaadinAppConfiguration(testContext) || !alreadySetCurrentUI(testContext)) {
            logger.debug("No need to clear MockUI for test context [{}]", testContext);
            return;
        }
        MockUI.tearDown();
        testContext.removeAttribute(SET_CURRENT_UI_ATTRIBUTE);
    }
}
