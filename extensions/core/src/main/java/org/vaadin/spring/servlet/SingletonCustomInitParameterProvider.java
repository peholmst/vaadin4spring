/*
 * Copyright 2016 The original authors
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
package org.vaadin.spring.servlet;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of {@link CustomInitParameterProvider} that provides a single init parameter.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class SingletonCustomInitParameterProvider implements CustomInitParameterProvider {

    private final String parameterName;
    private final String parameterValue;

    public SingletonCustomInitParameterProvider(String parameterName, String parameterValue) {
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    @Override
    public boolean containsInitParameter(String parameterName) {
        return this.parameterName.equals(parameterName);
    }

    @Override
    public String getInitParameter(String parameterName) {
        if (this.parameterName.equals(parameterName)) {
            return parameterValue;
        }
        return null;
    }

    @Override
    public Collection<String> getInitParameterNames() {
        return Collections.singleton(parameterName);
    }
}
