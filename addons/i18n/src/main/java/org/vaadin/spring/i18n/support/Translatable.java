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
package org.vaadin.spring.i18n.support;

import java.io.Serializable;
import java.util.Locale;

/**
 * Interface to be implemented by all components that contain some kind of internationalized content that needs to be
 * updated on the fly when the locale is changed.
 * 
 * @see TranslatableSupport
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface Translatable extends Serializable {

    /**
     * Called when the component should update all of its translatable strings, setting locales, etc. The locale to use
     * 
     * @param locale the new locale to use.
     */
    void updateMessageStrings(Locale locale);
}
