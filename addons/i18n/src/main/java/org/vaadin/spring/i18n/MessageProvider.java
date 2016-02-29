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
package org.vaadin.spring.i18n;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * A {@code MessageProvider} provides messages for a {@link org.vaadin.spring.i18n.CompositeMessageSource}.
 * There can be multiple message provider beans in the same application context.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public interface MessageProvider {

    /**
     * Attempts to resolve the specified code for the specified locale.
     *
     * @param s the code of the message, must not be {@code null}.
     * @param locale the locale, must not be {@code null}.
     * @return a {@code MessageFormat} for the message, or {@code null} if not found.
     */
    MessageFormat resolveCode(String s, Locale locale);

    /**
     * Clears any internal caches, forcing the message provider to resolve the codes from the original message source.
     */
    void clearCache();
}
