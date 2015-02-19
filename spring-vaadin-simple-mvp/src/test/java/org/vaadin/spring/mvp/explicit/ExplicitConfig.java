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
package org.vaadin.spring.mvp.explicit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.EnableVaadin;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EnableVaadinEventBus;
import org.vaadin.spring.mvp.FooView;

@Configuration
@EnableVaadin
@EnableVaadinEventBus
public class ExplicitConfig {

    @Autowired
    private EventBus.UIEventBus eventBus;

    @Bean
    @VaadinUIScope
    public FooView fooView() {
        return new FooView();
    }

    @Bean
    @VaadinUIScope
    public ExplicitPresenter fooPresenter() {
        return new ExplicitPresenter(fooView(), eventBus);
    }
}
