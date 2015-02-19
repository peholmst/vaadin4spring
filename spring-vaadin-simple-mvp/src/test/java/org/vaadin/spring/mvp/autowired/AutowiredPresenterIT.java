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
package org.vaadin.spring.mvp.autowired;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Integration Test that tests auto-wiring usage of the {@link org.vaadin.spring.mvp.Presenter} class.
 *
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ScanConfig.class)
@VaadinAppConfiguration
public class AutowiredPresenterIT {

    @Autowired
    private AutowiredPresenter autowiredPresenter;

    @Autowired
    private EventBus.UIEventBus eventBus;

    @Test
    public void should_listen_to_message() {
        String message = "message_from_autowired";
        eventBus.publish(this, message);
        assertEquals(message, autowiredPresenter.getView().getCaption());
    }
}
