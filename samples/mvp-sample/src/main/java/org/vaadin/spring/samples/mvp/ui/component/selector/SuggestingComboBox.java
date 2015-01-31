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
package org.vaadin.spring.samples.mvp.ui.component.selector;

import org.vaadin.spring.samples.mvp.ui.component.selector.SuggestingContainer.SuggestionFilter;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

public class SuggestingComboBox extends ComboBox {

    private static final long serialVersionUID = 8935599681528959687L;

    /**
     * Overwrite the protected method
     * {@link ComboBox#buildFilter(String, FilteringMode)} to return a custom
     * {@link SuggestionFilter} which is only needed to pass the given
     * filterString on to the {@link SuggestingContainer}.
     */
    @Override
    protected Filter buildFilter(String filterString,
            FilteringMode filteringMode) {
        return new SuggestingContainer.SuggestionFilter(filterString);
    }
}
