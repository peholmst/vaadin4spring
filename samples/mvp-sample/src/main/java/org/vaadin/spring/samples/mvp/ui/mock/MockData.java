package org.vaadin.spring.samples.mvp.ui.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.vaadin.spring.samples.mvp.dto.CommitStatusType;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateHourlyDTO;

@Component
public class MockData {

    private static Map<String, DateTime> PARTICIPANTS = new HashMap<>();
    public static DateTime TODAY = new DateTime();
    public static DateTime TERMINATION_DATE = TODAY.plusDays(365);

    static {
        DateTime yesterday = TODAY.minusDays(1);
        PARTICIPANTS.put("U0135H", yesterday);
        PARTICIPANTS.put("U0336H", TERMINATION_DATE);
        PARTICIPANTS.put("U0567H", TERMINATION_DATE);
    }

    public Map<String, DateTime> allParticipants() {
        return PARTICIPANTS;
    }

    public List<DSRUpdateHourlyDTO> allDsrHourlyUpdates() {
        List<DSRUpdateHourlyDTO> beans = new ArrayList<>();

        DSRUpdateHourlyDTO dto = null;
        String[] commitStatii = new String[] { CommitStatusType.ECONOMIC.getValue(), CommitStatusType.MUST_RUN.getValue(), CommitStatusType.UNAVAILABLE.getValue(),
                CommitStatusType.UNAVAILABLE.getValue(), CommitStatusType.UNAVAILABLE.getValue(), CommitStatusType.ECONOMIC.getValue(),
                CommitStatusType.ECONOMIC.getValue(), CommitStatusType.MUST_RUN.getValue(), CommitStatusType.UNAVAILABLE.getValue(),
                CommitStatusType.ECONOMIC.getValue(), CommitStatusType.ECONOMIC.getValue(), CommitStatusType.ECONOMIC.getValue(),
                CommitStatusType.ECONOMIC.getValue(), CommitStatusType.ECONOMIC.getValue(), CommitStatusType.ECONOMIC.getValue(),
                CommitStatusType.MUST_RUN.getValue(), CommitStatusType.MUST_RUN.getValue(), CommitStatusType.MUST_RUN.getValue(),
                CommitStatusType.MUST_RUN.getValue(), CommitStatusType.MUST_RUN.getValue(), CommitStatusType.MUST_RUN.getValue(),
                CommitStatusType.ECONOMIC.getValue(), CommitStatusType.MUST_RUN.getValue(), CommitStatusType.UNAVAILABLE.getValue()
        };
        BigDecimal[] ecoMin = new BigDecimal[] { new BigDecimal(10.1), new BigDecimal(10.2), new BigDecimal(10.3),
                new BigDecimal(10.1), new BigDecimal(10.1), new BigDecimal(10.4),
                new BigDecimal(10.4), new BigDecimal(10.5), new BigDecimal(10.1),
                new BigDecimal(10.1), new BigDecimal(10.2), new BigDecimal(10.2),
                new BigDecimal(10.2), new BigDecimal(10.2), new BigDecimal(10.6),
                new BigDecimal(10.5), new BigDecimal(10.2), new BigDecimal(10.5),
                new BigDecimal(10.5), new BigDecimal(10.5), new BigDecimal(10.5),
                new BigDecimal(10.3), new BigDecimal(10.2), new BigDecimal(10.4)};
        BigDecimal[] ecoMax = new BigDecimal[] { new BigDecimal(15.1), new BigDecimal(15.2), new BigDecimal(15.3),
                new BigDecimal(15.1), new BigDecimal(15.1), new BigDecimal(15.4),
                new BigDecimal(15.4), new BigDecimal(15.5), new BigDecimal(15.1),
                new BigDecimal(15.1), new BigDecimal(15.2), new BigDecimal(15.2),
                new BigDecimal(15.2), new BigDecimal(15.2), new BigDecimal(15.6),
                new BigDecimal(15.5), new BigDecimal(15.2), new BigDecimal(15.5),
                new BigDecimal(15.5), new BigDecimal(15.5), new BigDecimal(15.5),
                new BigDecimal(15.3), new BigDecimal(15.2), new BigDecimal(15.4)};

        String[] hours = new String[] {"24", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

        String[] locations = new String[] { "Bruderheim", "Immerhof", "Desseldorf"};

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 24; i++) {
                dto = new DSRUpdateHourlyDTO();
                dto.setCommitStatus(commitStatii[i]);
                dto.setEconomicMax(ecoMax[i]);
                dto.setEconomicMin(ecoMin[i]);
                dto.getId().setHour(hours[i]);
                dto.getId().setAssetOwner("U0336H");
                dto.getId().setLocation(locations[j]);
                beans.add(dto);
            }
        }

        for (int i = 0; i < 24; i++) {
            dto = new DSRUpdateHourlyDTO();
            dto.setCommitStatus(commitStatii[i]);
            dto.setEconomicMax(ecoMax[i]);
            dto.setEconomicMin(ecoMin[i]);
            dto.getId().setHour(hours[i]);
            dto.getId().setAssetOwner("U0567H");
            dto.getId().setLocation(locations[2]);
            beans.add(dto);
        }

        return beans;
    }

}
