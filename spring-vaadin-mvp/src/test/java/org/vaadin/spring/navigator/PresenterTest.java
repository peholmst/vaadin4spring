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
package org.vaadin.spring.navigator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.test.annotation.VaadinAppConfiguration;


/**
 * Basic test for proper functioning of an implementor of {@link Presenter}.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@VaadinAppConfiguration
public class PresenterTest {

    @Autowired
    private FooPresenter presenter;

    @Autowired
    private SpringViewProvider provider;

    @Autowired
    private EventBus.UIEventBus eventBus;

    @Test
    public void testValidPresenter() {
        Assert.assertNotNull(presenter);
        Assert.assertNotNull(presenter.getEventBus());
        Assert.assertTrue(presenter.getView().equals(provider.getView(FooView.NAME)));

        String message = "Some foo for you";
        eventBus.publish(this, message);

        Assert.assertEquals(message, presenter.getView().getFoo());
    }

}
