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

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;

/**
 * This is a specialized {@link BeanItemContainer} which redefines the filtering
 * functionality by overwriting method
 * {@link com.vaadin.data.util.AbstractInMemoryContainer#addFilter(Filter)}.
 * This method is called internally by the filtering code of a ComboBox.
 */
public abstract class SuggestingContainer<BEANTYPE, SERVICE> extends BeanItemContainer<BEANTYPE> {

    private static final long serialVersionUID = -7702366917056870298L;

    protected SERVICE service;

    public SuggestingContainer(Class<? super BEANTYPE> type, SERVICE service) throws IllegalArgumentException {
        super(type);
        this.service = service;
    }

    /**
     * This method will be called by ComboBox each time the user has entered a new
     * value into the text field of the ComboBox. For our custom ComboBox class
     * {@link SuggestingComboBox} it is assured by
     * {@link SuggestingComboBox#buildFilter(String, com.vaadin.shared.ui.combobox.FilteringMode)}
     * that only instances of {@link SuggestionFilter} are passed into this
     * method. We can therefore safely cast the filter to this class. Then we
     * simply get the filterString from this filter and call the database service
     * with this filterString. The database then returns a list of transfer objects
     * whose names begin with the filterString. After having removed all
     * existing items from the container we add the new list of freshly filtered
     * country objects.
     */
    @Override
    protected void addFilter(Filter filter) throws UnsupportedFilterException {
        SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
        filterItems(suggestionFilter.getFilterString());
    }

    private void filterItems(String filterString) {
        removeAllItems();
        List<BEANTYPE> beans = callService(filterString);
        addAll(beans);
    }

    protected abstract List<BEANTYPE> callService(String filterString);


    /**
     * The sole purpose of this {@link Filter} implementation is to transport the
     * current filterString (which is a private property of ComboBox) to our
     * custom container implementation {@link SuggestingContainer}. Our container
     * needs that filterString in order to fetch a filtered country list from the
     * database.
     */
    public static class SuggestionFilter implements Container.Filter {

        private static final long serialVersionUID = 3643966422244708411L;

        private String filterString;

        public SuggestionFilter(String filterString) {
            this.filterString = filterString;
        }

        public String getFilterString() {
            return filterString;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            // will never be used and can hence always return false
            return false;
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            // will never be used and can hence always return false
            return false;
        }

    }
}

