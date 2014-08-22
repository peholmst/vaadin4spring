package org.vaadin.spring.samples.mvp.ui.mock;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

public class MockData {

    public static final Map<String, DateTime> PARTICIPANTS;

    static {
        DateTime today = new DateTime();
        DateTime noLongerEffectiveAsOf = today.plusDays(365);
        PARTICIPANTS = new HashMap<>();
        PARTICIPANTS.put("U0135H", SSTimeUtil.isoDayToDateTime("2014-08-19T00:00:00"));
        PARTICIPANTS.put("U0336H", noLongerEffectiveAsOf);
        PARTICIPANTS.put("U0567H", noLongerEffectiveAsOf);
    }
}
