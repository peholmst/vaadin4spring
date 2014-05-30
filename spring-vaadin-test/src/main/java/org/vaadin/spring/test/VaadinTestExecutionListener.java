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

import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.vaadin.spring.internal.UIID;
import org.vaadin.spring.internal.UIScope;

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
    private int nextUIId = 0;

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
        testContext.removeAttribute(SET_CURRENT_UI_ATTRIBUTE);
        logger.debug("Detaching MockUI [{}] after test", UI.getCurrent());
        UI.getCurrent().detach();
        UI.setCurrent(null);
        CurrentInstance.set(UIID.class, null);
    }

    private boolean notAnnotatedWithVaadinAppConfiguration(TestContext testContext) {
        return AnnotationUtils.findAnnotation(testContext.getTestClass(), VaadinAppConfiguration.class) == null;
    }

    private boolean alreadySetCurrentUI(TestContext testContext) {
        return Boolean.TRUE.equals(testContext.getAttribute(SET_CURRENT_UI_ATTRIBUTE));
    }

    private synchronized void setCurrentUIIfNecessary(TestContext testContext) {
        if (notAnnotatedWithVaadinAppConfiguration(testContext) || alreadySetCurrentUI(testContext)) {
            return;
        }

        final ApplicationContext context = testContext.getApplicationContext();

        if (context instanceof ConfigurableApplicationContext) {
            final ConfigurableApplicationContext cac = (ConfigurableApplicationContext) context;
            final ConfigurableListableBeanFactory bf = cac.getBeanFactory();

            logger.debug("Setting up MockUI for test context [{}]", testContext);

            final MockUI ui = new MockUI(++nextUIId);
            UI.setCurrent(ui);
            CurrentInstance.set(UIID.class, ui.getUiIdentifier());
            // Register the UI with the UI Scope (to get detach events, etc.)
            bf.getRegisteredScope(UIScope.UI_SCOPE_NAME).get("mockUI", new ObjectFactory<MockUI>() {
                @Override
                public MockUI getObject() throws BeansException {
                    return ui;
                }
            });
            testContext.setAttribute(SET_CURRENT_UI_ATTRIBUTE, Boolean.TRUE);
        }
    }
}
