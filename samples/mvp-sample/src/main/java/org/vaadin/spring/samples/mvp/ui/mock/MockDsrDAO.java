package org.vaadin.spring.samples.mvp.ui.mock;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateDTO;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateHourlyDTO;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

@Repository
public class MockDsrDAO implements DsrDAO {

    @Inject
    MockData data;

    @Override
    // construct data for any day up to effective date of assetOwner
    public List<DSRUpdateDTO> getDSRHourly(DateTime day, String assetOwner) {
        List<DSRUpdateDTO> result = new ArrayList<>();
        DSRUpdateDTO dto = new DSRUpdateDTO();
        List<DSRUpdateHourlyDTO> hourlies = new ArrayList<>();
        DSRUpdateHourlyDTO h;
        DateTime hour;
        if (data.allParticipants().keySet().contains(assetOwner) && day.isBefore(MockData.TERMINATION_DATE)) {
            dto = new DSRUpdateDTO();
            dto.getId().setDay(SSTimeUtil.dateTimeToIsoDay(day));
            for (DSRUpdateHourlyDTO hourly: data.allDsrHourlyUpdates()) {
                if (assetOwner.equals(hourly.getId().getAssetOwner())) {
                    h = new DSRUpdateHourlyDTO();
                    h.setCommitStatus(hourly.getCommitStatus());
                    h.setEconomicMax(hourly.getEconomicMax());
                    h.setEconomicMin(hourly.getEconomicMin());
                    h.getId().setAssetOwner(hourly.getId().getAssetOwner());
                    h.getId().setLocation(hourly.getId().getLocation());
                    int parsedHour = Integer.parseInt(hourly.getId().getHour());
                    if (parsedHour >= 0 && parsedHour < 24) {
                        hour = day.plusHours(parsedHour);
                    } else {
                        hour = day.plusDays(1);
                    }
                    h.getId().setHour(SSTimeUtil.dateTimeToIsoNoMillis(hour));
                    hourlies.add(h);
                }
            }
            dto.setRecords(hourlies);
            result.add(dto);
        }
        return result;
    }

}
