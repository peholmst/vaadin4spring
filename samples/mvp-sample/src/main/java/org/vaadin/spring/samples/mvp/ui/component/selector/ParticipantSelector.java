package org.vaadin.spring.samples.mvp.ui.component.selector;

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

import com.vaadin.navigator.View;


public abstract class ParticipantSelector extends SuggestingComboBox implements View {

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
