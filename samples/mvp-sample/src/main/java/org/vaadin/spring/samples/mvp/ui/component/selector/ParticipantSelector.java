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

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.navigator.View;


public abstract class ParticipantSelector extends SuggestingComboBox implements View {

    private static final long serialVersionUID = -6972162433727711612L;

    private static final DateTime DEFAULT_MARKET_DAY = SSTimeUtil.getMarketStartDateTime();
    private static final String DEFAULT_CAPTION = "Participant";

    protected DateTime effectiveDay;

    public ParticipantSelector() {
        // the item caption mode has to be PROPERTY for the filtering to work
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setCaption(DEFAULT_CAPTION);
        // define the property name of the participant to use as item caption
        setItemCaptionPropertyId("assetOwner");
        setEffectiveDay(DEFAULT_MARKET_DAY);
        setNullSelectionAllowed(false);
        setImmediate(true);
    }

    public void setEffectiveDay(DateTime effectiveDay) {
        this.effectiveDay = effectiveDay;
    }


}
