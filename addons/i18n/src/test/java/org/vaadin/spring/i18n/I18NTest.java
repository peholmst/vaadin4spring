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

import com.vaadin.ui.UI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link org.vaadin.spring.i18n.I18N}.
 */
@SuppressWarnings("unchecked")
public class I18NTest {

    @Mock
    ApplicationContext applicationContext;

    @Mock
    UI ui;

    I18N i18n;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        i18n = new I18N(applicationContext);
        UI.setCurrent(null);
    }

    @Test
    public void get_noCurrentUIAndMessageIsResolved_messageIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenReturn("myMessage");

        assertNull(UI.getCurrent());
        assertEquals("myMessage", i18n.get("myCode", "myArg"));
    }

    @Test
    public void get_noCurrentUIAndMessageIsResolvedInDefaultBundleAndRevertToDefaultBundleIsTrue_messageIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenThrow(NoSuchMessageException.class);
        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, null)).thenReturn("myMessage");

        assertNull(UI.getCurrent());
        assertEquals("myMessage", i18n.get("myCode", "myArg"));
    }

    @Test
    public void get_noCurrentUIAndMessageIsNotResolvedAndRevertToDefaultBundleIsTrue_codeIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenThrow(NoSuchMessageException.class);
        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, null)).thenThrow(NoSuchMessageException.class);

        assertNull(UI.getCurrent());
        assertEquals("!myCode", i18n.get("myCode", "myArg"));
    }

    @Test
    public void get_noCurrentUIAndMessageIsNotResolvedAndRevertToDefaultBundleIsFalse_codeIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenThrow(NoSuchMessageException.class);

        assertNull(UI.getCurrent());
        i18n.setRevertToDefaultBundle(false);
        assertEquals("!myCode", i18n.get("myCode", "myArg"));
    }

    @Test
    public void getWithDefault_noCurrentUIAndMessageIsResolved_messageIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenReturn("myMessage");

        assertNull(UI.getCurrent());
        assertEquals("myMessage", i18n.getWithDefault("myCode", "myDefaultMsg", "myArg"));
    }

    @Test
    public void getWithDefault_noCurrentUIAndMessageIsResolvedInDefaultBundleAndRevertToDefaultBundleIsTrue_messageIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenThrow(NoSuchMessageException.class);
        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, null)).thenReturn("myMessage");

        assertNull(UI.getCurrent());
        assertEquals("myMessage", i18n.getWithDefault("myCode", "myDefaultMsg", "myArg"));
    }

    @Test
    public void getWithDefault_noCurrentUIAndMessageIsNotResolvedAndRevertToDefaultBundleIsTrue_defaultMessageIsReturned() {
        final Locale locale = Locale.getDefault();

        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, locale)).thenThrow(NoSuchMessageException.class);
        when(applicationContext.getMessage("myCode", new Object[] {"myArg"}, null)).thenThrow(NoSuchMessageException.class);

        assertNull(UI.getCurrent());
        assertEquals("myDefaultMsg", i18n.getWithDefault("myCode", "myDefaultMsg", "myArg"));
    }

    @Test
    public void getLocale_noCurrentUI_defaultLocaleIsReturned() {
        final Locale locale = Locale.getDefault();
        assertNull(UI.getCurrent());
        assertEquals(locale, i18n.getLocale());
    }

    @Test
    public void getLocale_currentUI_uiLocaleIsReturned() {
        final Locale locale = Locale.FRANCE;
        when(ui.getLocale()).thenReturn(locale);
        UI.setCurrent(ui);
        assertEquals(locale, i18n.getLocale());
    }
}
