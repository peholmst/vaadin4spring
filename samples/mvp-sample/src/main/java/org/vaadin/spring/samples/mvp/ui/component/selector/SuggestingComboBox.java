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
