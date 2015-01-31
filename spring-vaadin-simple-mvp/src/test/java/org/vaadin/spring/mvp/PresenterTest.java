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
package org.vaadin.spring.mvp;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.mvp.explicit.ExplicitPresenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit Test for the {@link org.vaadin.spring.mvp.Presenter} class.
 *
 * @author Nicolas Frankel (nicolas@frankel.ch)
 */
public class PresenterTest {

    private ExplicitPresenter presenter;
    private FooView fooView;
    private EventBus eventBus;

    @Before
    public void setUp() {
        fooView = new FooView();
        eventBus = mock(EventBus.class);
        presenter = new ExplicitPresenter(fooView, eventBus);
    }

    @Test
    public void should_manage_underlying_view() {
        presenter.onNewCaption("test");
        assertEquals("test", fooView.getCaption());
    }
}
