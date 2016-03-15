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

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;

import com.vaadin.ui.UI;

/**
 * Helper class for resolving messages in a Vaadin UI. This is effectively a wrapper around
 * {@link org.springframework.context.ApplicationContext}
 * that uses the locale of the current UI to lookup messages. Use it like this:
 * 
 * <pre>
 * &#64;VaadinUI
 * public class MyUI extends UI {
 *
 *     &#64;Autowired I18N i18n;
 *
 *     ...
 *
 *     void init() {
 *         myLabel.setCaption(i18n.get("myLabel.caption"));
 *     }
 * }
 * </pre>
 * 
 * Please note, that you also need to configure a {@link org.springframework.context.MessageSource} inside your
 * application context
 * that contains all the messages to resolve.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class I18N {

    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean revertToDefaultBundle = true;

    /**
     * @param applicationContext the application context to read messages from, never {@code null}.
     */
    @Autowired
    public I18N(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Returns whether {@code I18N} will try the default bundle if a message cannot be resolved for the
     * current locale. By default, this is true.
     */
    public boolean isRevertToDefaultBundle() {
        return revertToDefaultBundle;
    }

    /**
     * See {@link #isRevertToDefaultBundle()}.
     */
    public void setRevertToDefaultBundle(boolean revertToDefaultBundle) {
        this.revertToDefaultBundle = revertToDefaultBundle;
    }

    /**
     * Tries to resolve the specified message for the current locale.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet', never {@code null}.
     * @param arguments Array of arguments that will be filled in for params within the message (params look like "{0}",
     *        "{1,date}", "{2,time}"), or {@code null} if none.
     * @return the resolved message, or the message code prepended with an exclamation mark if the lookup fails.
     * @see org.springframework.context.ApplicationContext#getMessage(String, Object[], java.util.Locale)
     * @see #getLocale()
     */
    public String get(String code, Object... arguments) {
        try {
            return getMessage(code, null, arguments);
        } catch (NoSuchMessageException ex) {
            logger.warn("Tried to retrieve message with code [{}] that does not exist", code);
            return "!" + code;
        }
    }

    /**
     * Tries to resolve the specified message for the given locale.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet', never {@code null}.
     * @param locale The Locale for which it is tried to look up the code. Must not be null
     * @param arguments Array of arguments that will be filled in for params within the message (params look like "{0}",
     *        "{1,date}", "{2,time}"), or {@code null} if none.
     * @throws IllegalArgumentException if the given Locale is null
     * @return the resolved message, or the message code prepended with an exclamation mark if the lookup fails.
     * @see org.springframework.context.ApplicationContext#getMessage(String, Object[], java.util.Locale)
     * @see java.util.Locale
     */
    public String get(String code, Locale locale, Object... arguments) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale must not be null");
        }
        try {
            return getMessage(code, locale, arguments);
        } catch (NoSuchMessageException ex) {
            logger.warn("Tried to retrieve message with code [{}] that does not exist", code);
            return "!" + code;
        }
    }

    /**
     * Tries to resolve the specified message for the given locale.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet', never {@code null}.
     * @param locale The Locale for which it is tried to look up the code. Must not be null
     * @param arguments Array of arguments that will be filled in for params within the message (params look like "{0}",
     *        "{1,date}", "{2,time}"), or {@code null} if none.
     * @throws IllegalArgumentException if the given Locale is null
     * @return the resolved message, or the message code prepended with an exclamation mark if the lookup fails.
     * @see org.springframework.context.ApplicationContext#getMessage(String, Object[], java.util.Locale)
     * @see java.util.Locale
     * @deprecated Use {@link #get(String, Locale, Object...)} instead.
     */
    @Deprecated
    public String getWithLocale(String code, Locale locale, Object... arguments) {
        return get(code, locale, arguments);
    }

    /**
     * Tries to resolve the specified message for the current locale.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet', never {@code null}.
     * @param defaultMessage string to return if the lookup fails, never {@code null}.
     * @param arguments Array of arguments that will be filled in for params within the message (params look like "{0}",
     *        "{1,date}", "{2,time}"), or {@code null} if none.
     * @return the resolved message, or the {@code defaultMessage} if the lookup fails.
     * @see #getLocale()
     */
    public String getWithDefault(String code, String defaultMessage, Object... arguments) {
        try {
            return getMessage(code, null, arguments);
        } catch (NoSuchMessageException ex) {
            return defaultMessage;
        }
    }

    /**
     * Tries to resolve the specified message for the given locale.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet', never {@code null}.
     * @param locale The Locale for which it is tried to look up the code. Must not be null
     * @param defaultMessage string to return if the lookup fails, never {@code null}.
     * @param arguments Array of arguments that will be filled in for params within the message (params look like "{0}",
     *        "{1,date}", "{2,time}"), or {@code null} if none.
     * @return the resolved message, or the {@code defaultMessage} if the lookup fails.
     */
    public String getWithDefault(String code, Locale locale, String defaultMessage, Object... arguments) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale must not be null");
        }
        try {
            return getMessage(code, locale, arguments);
        } catch (NoSuchMessageException ex) {
            return defaultMessage;
        }
    }

    private String getMessage(String code, Locale locale, Object... arguments) {
        Locale actualLocale = locale == null ? getLocale() : locale;
        try {
            return applicationContext.getMessage(code, arguments, actualLocale);
        } catch (NoSuchMessageException ex) {
            if (isRevertToDefaultBundle()) {
                return applicationContext.getMessage(code, arguments, null);
            } else {
                throw ex;
            }
        }
    }

    /**
     * Gets the locale of the current Vaadin UI. If the locale can not be determinted, the default locale
     * is returned instead.
     *
     * @return the current locale, never {@code null}.
     * @see com.vaadin.ui.UI#getLocale()
     * @see java.util.Locale#getDefault()
     */
    public Locale getLocale() {
        UI currentUI = UI.getCurrent();
        Locale locale = currentUI == null ? null : currentUI.getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }
}
