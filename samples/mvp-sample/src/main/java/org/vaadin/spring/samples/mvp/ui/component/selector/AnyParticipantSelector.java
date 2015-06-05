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

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;
import org.vaadin.spring.samples.mvp.ui.service.UiService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@UIScope
@SpringView(name = AnyParticipantSelector.NAME)
public class AnyParticipantSelector extends ParticipantSelector {

    private static final long serialVersionUID = 601560875854451663L;

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

        private static final long serialVersionUID = -2896828348082362946L;

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
