package org.vaadin.spring.samples.mvp.ui.mock;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class MockData {

    private static Map<String, DateTime> PARTICIPANTS = new HashMap<>();

    static {
        DateTime today = new DateTime();
        DateTime noLongerEffectiveAsOf = today.plusDays(365);
        DateTime yesterday = today.minusDays(1);
        PARTICIPANTS.put("U0135H", yesterday);
        PARTICIPANTS.put("U0336H", noLongerEffectiveAsOf);
        PARTICIPANTS.put("U0567H", noLongerEffectiveAsOf);
    }

    public Map<String, DateTime> allParticipants() {
        return PARTICIPANTS;
    }
}
