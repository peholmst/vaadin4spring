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

import org.vaadin.spring.navigator.annotation.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Panel;

@VaadinUIScope
@VaadinView(name = FooView.NAME)
class FooView extends Panel implements View {

    private static final long serialVersionUID = 3893006670519931367L;

    public static final String NAME = "foo";

    private String foo;

    /**
     * Sets this Panel's caption
     * (and also sets state of foo variable)
     * @param someFoo a caption
     */
    public void setFoo(String foo) {
        this.foo = foo;
        setCaption(foo);
    }

    // normally one wouldn't provide an accessor like this
    // this is here to verify state was properly set
    public String getFoo() {
        return foo;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}
