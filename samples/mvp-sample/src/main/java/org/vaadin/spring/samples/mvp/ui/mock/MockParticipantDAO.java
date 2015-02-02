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
package org.vaadin.spring.samples.mvp.ui.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;
import org.vaadin.spring.samples.mvp.ui.dao.ParticipantDAO;
import org.vaadin.spring.samples.mvp.util.SSTimeUtil;

@Repository
public class MockParticipantDAO implements ParticipantDAO {

    private static Logger log = LoggerFactory.getLogger(MockParticipantDAO.class);

    @Inject
    MockData data;

    private List<AssetOwnedDailyId> getParticipantsInternal(@NotNull final String name, @NotNull final DateTime day) {
        log.debug("Retrieving participants whose names start with [{}] that are effective on [{}].", name, day);
        List<AssetOwnedDailyId> result = new ArrayList<>();
        AssetOwnedDailyId participant;
        String assetOwner;
        DateTime d;
        for (Map.Entry<String, DateTime> entry: data.allParticipants().entrySet()) {
            assetOwner = entry.getKey();
            d = entry.getValue();
            if (assetOwner.startsWith(name.toUpperCase()) && day.isBefore(d)) {
                participant = new AssetOwnedDailyId();
                participant.setAssetOwner(assetOwner);
                participant.setDay(SSTimeUtil.dateTimeToIsoDay(d));
                result.add(participant);
            }
        }
        return result;
    }

    @Override
    public List<AssetOwnedDailyId> getParticipants(String name, DateTime day) {
        return getParticipantsInternal(name, day);
    }

}
