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
package org.vaadin.spring.stuff.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Message source that resolves the messages by querying the {@link org.vaadin.spring.stuff.i18n.MessageProvider}s in
 * the application context. The resolved messages are cached.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class CompositeMessageSource extends AbstractMessageSource implements MessageSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeMessageSource.class);

    private final Collection<MessageProvider> messageProviders;

    private final Map<Locale, Map<String, MessageFormat>> messageFormatCache = new ConcurrentHashMap<>();

    /**
     * Creates a new {@code CompositeMessageSource}.
     *
     * @param applicationContext the application context to use when looking up {@link org.vaadin.spring.stuff.i18n.MessageProvider}s, must not be {@code null}.
     */
    public CompositeMessageSource(ApplicationContext applicationContext) {
        LOGGER.info("Looking up MessageProviders");
        messageProviders = applicationContext.getBeansOfType(MessageProvider.class).values();
        if (LOGGER.isDebugEnabled()) {
            for (MessageProvider messageProvider : messageProviders) {
                LOGGER.debug("Found MessageProvider [{}]", messageProvider);
            }
        }
        LOGGER.info("Found {} MessageProvider(s)", messageProviders.size());
    }

    @Override
    protected MessageFormat resolveCode(String s, Locale locale) {
        MessageFormat messageFormat = queryCache(s, locale);
        if (messageFormat == null) {
            messageFormat = queryMessageProviders(s, locale);
            if (messageFormat != null) {
                cache(s, locale, messageFormat);
            }
        }
        return messageFormat;
    }

    private MessageFormat queryCache(String s, Locale locale) {
        final Map<String, MessageFormat> cache = getMessageFormatCache(locale);
        return cache.get(s);
    }

    private void cache(String s, Locale locale, MessageFormat messageFormat) {
        final Map<String, MessageFormat> cache = getMessageFormatCache(locale);
        cache.put(s, messageFormat);
    }

    private MessageFormat queryMessageProviders(String s, Locale locale) {
        LOGGER.debug("Querying message providers for code [{}] for locale [{}]", s, locale);
        for (MessageProvider messageProvider : messageProviders) {
            final MessageFormat messageFormat = messageProvider.resolveCode(s, locale);
            if (messageFormat != null) {
                LOGGER.debug("Code [{}] for locale [{}] found in provider [{}]", s, locale, messageProvider);
                return messageFormat;
            }
        }
        LOGGER.debug("Code [{}] for locale [{}] not found", s, locale);
        return null;
    }

    private Map<String, MessageFormat> getMessageFormatCache(Locale locale) {
        Map<String, MessageFormat> cache = messageFormatCache.get(locale);
        if (cache == null) {
            cache = new ConcurrentHashMap<>();
            messageFormatCache.put(locale, cache);
        }
        return cache;
    }
}
