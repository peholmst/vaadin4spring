package org.vaadin.spring.samples.mvp.ui.component.selector;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.spring.VaadinUIScope;
import org.vaadin.spring.navigator.VaadinView;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;
import org.vaadin.spring.samples.mvp.ui.service.UiService;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;


@VaadinUIScope
@VaadinView(name = AnyParticipantSelector.NAME)
public class AnyParticipantSelector extends ParticipantSelector {

    public static final String NAME = "anyParticipantSelector";

    @Inject
    UiService uiService;

    @PostConstruct
    private void init() {
        // service is not available (i.e., not injected) until after SuggestionComboBox is instantiated
        setContainerDataSource(new ParticipantSuggestingContainer(AssetOwnedDailyId.class, uiService));
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }


    private class ParticipantSuggestingContainer extends SuggestingContainer<AssetOwnedDailyId, UiService> {

        public ParticipantSuggestingContainer(
                Class<? super AssetOwnedDailyId> type, UiService service)
                        throws IllegalArgumentException {
            super(type, service);
        }

        @Override
        protected List<AssetOwnedDailyId> callService(String filterString) {
            List<AssetOwnedDailyId> result = new ArrayList<>();
            if (StringUtils.isNotBlank(filterString)) {
                result.addAll(service.getParticipants(filterString, effectiveDay));
            }
            return result;
        }

    }
}
