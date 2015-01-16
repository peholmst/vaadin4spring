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
package org.vaadin.spring.i18n.support;

/**
 * Names of common Vaadin component properties that you might want to translate. These constants
 * are provided to reduce the risk of typos when specifying the {@link org.vaadin.spring.i18n.annotation.TranslatedProperty#property()} attribute.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public abstract class ComponentProperties {

    protected ComponentProperties() {
    }

    /**
     * @see com.vaadin.ui.AbstractComponent#setCaption(String)
     */
    public static final String CAPTION = "caption";
    /**
     * @see com.vaadin.ui.AbstractComponent#setDescription(String)
     */
    public static final String DESCRIPTION = "description";

    /**
     * @see com.vaadin.ui.AbstractTextField#setNullRepresentation(String)
     */
    public static final String NULL_REPRESENTATION = "nullRepresentation";

    /**
     * @see com.vaadin.ui.AbstractTextField#setInputPrompt(String)
     */
    public static final String INPUT_PROMPT = "inputPrompt";

    /**
     * @see com.vaadin.ui.Label#setValue(String)
     */
    public static final String VALUE = "value";
}
