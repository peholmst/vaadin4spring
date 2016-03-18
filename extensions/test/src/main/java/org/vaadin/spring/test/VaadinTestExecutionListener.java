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
package org.vaadin.spring.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;

/**
 * @author Petter Holmström (petter@vaadin.com)
 */
public class VaadinTestExecutionListener extends AbstractTestExecutionListener {

    public static final String SET_UP_SCOPES_ATTRIBUTE = Conventions.getQualifiedAttributeName(
            VaadinTestExecutionListener.class, "setUpScopes");
    private static final Logger logger = LoggerFactory.getLogger(VaadinTestExecutionListener.class);

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        setUpVaadinScopesIfNecessary(testContext);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        setUpVaadinScopesIfNecessary(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        tearDownVaadinScopesIfNecessary(testContext);
    }

    private boolean notAnnotatedWithVaadinAppConfiguration(TestContext testContext) {
        return AnnotationUtils.findAnnotation(testContext.getTestClass(), VaadinAppConfiguration.class) == null;
    }

    private static boolean alreadySetUpVaadinScopes(TestContext testContext) {
        return Boolean.TRUE.equals(testContext.getAttribute(SET_UP_SCOPES_ATTRIBUTE));
    }

    private synchronized void setUpVaadinScopesIfNecessary(TestContext testContext) {
        if (notAnnotatedWithVaadinAppConfiguration(testContext) || alreadySetUpVaadinScopes(testContext)) {
            logger.debug("No need to set up Vaadin scopes for test context [{}]", testContext);
            return;
        }

        VaadinScopes.setUp();
        testContext.setAttribute(SET_UP_SCOPES_ATTRIBUTE, Boolean.TRUE);
    }

    private synchronized void tearDownVaadinScopesIfNecessary(TestContext testContext) {
        if (notAnnotatedWithVaadinAppConfiguration(testContext) || !alreadySetUpVaadinScopes(testContext)) {
            logger.debug("No need to tear down Vaadin scopes for test context [{}]", testContext);
            return;
        }
        VaadinScopes.tearDown();
        testContext.removeAttribute(SET_UP_SCOPES_ATTRIBUTE);
    }
}
