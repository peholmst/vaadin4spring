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

import java.util.Locale;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * Implementation of {@link Translatable} intended to be used as a delegate by an owning {@link UI}.
 * The {@link #updateMessageStrings(Locale)} method will traverse the entire component hierarchy of the UI and
 * update the message strings of any components that implement the {@link Translatable} interface.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class TranslatableSupport implements Translatable {

    private final UI ui;

    /**
     * Creates a new {@code TranslatableSupport}.
     * 
     * @param ui the UI that owns the object.
     */
    public TranslatableSupport(UI ui) {
        this.ui = ui;
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        updateMessageStrings(locale, ui);
    }

    private void updateMessageStrings(Locale locale, Component component) {
        if (component instanceof Translatable) {
            ((Translatable) component).updateMessageStrings(locale);
        }
        if (component instanceof HasComponents) {
            for (Component child : (HasComponents) component) {
                updateMessageStrings(locale, child);
            }
        }
    }

}
