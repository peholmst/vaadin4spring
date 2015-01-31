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
package org.vaadin.spring.samples.mvp.ui.component.nav;


import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.vaadin.spring.samples.mvp.util.JSONUtil;

import com.google.gwt.thirdparty.guava.common.reflect.TypeToken;

/**
 * Constructs a list of navigation beans from a JSON file on the classpath
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public class NavElementFactory {

    @SuppressWarnings("serial")
    public List<NavElement> getNavElements(String resource) {
        Type collectionType = new TypeToken<Collection<NavElement>>(){}.getType();
        List<NavElement> result = JSONUtil.restoreFromJson(resource, collectionType);
        return result;
    }

}