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

/**
 * A custom init parameter provider is used by {@link Vaadin4SpringServlet} to provide custom servlet init parameter
 * values without having to redeclare the servlet registration or override the servlet itself.
 * 
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface CustomInitParameterProvider {

    /**
     * Returns whether this provider contains an init parameter with the specified name.
     * 
     * @param parameterName the name of the init parameter.
     * @return true if the parameter exists, false otherwise.
     */
    boolean containsInitParameter(String parameterName);

    /**
     * Returns the value of the specified init parameter name.
     * 
     * @param parameterName the name of the init parameter.
     * @return the value of the parameter name or {@code null} if no such parameter exists.
     */
    String getInitParameter(String parameterName);

    /**
     * Returns the names of all the init parameters in this provider.
     * 
     * @return a collection of parameter names.
     */
    Collection<String> getInitParameterNames();
}
