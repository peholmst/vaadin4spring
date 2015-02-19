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
package org.vaadin.spring.events.internal;

import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.support.ApplicationContextEventBroker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test case for {@link org.vaadin.spring.events.support.ApplicationContextEventBroker}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class ApplicationContextEventBrokerTest {

    @Test
    public void testOnApplicationEvent() {
        ApplicationEvent event = new ApplicationEvent(this) {

            private static final long serialVersionUID = 7475015652750718692L;

            @Override
            public Object getSource() {
                return "mySource";
            }
        };
        EventBus eventBus = mock(EventBus.class);
        new ApplicationContextEventBroker(eventBus).onApplicationEvent(event);
        verify(eventBus).publish("mySource", event);
    }

}
