package org.vaadin.spring.samples.mvp.ui.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;

public interface ParticipantDAO {

    List<AssetOwnedDailyId> getParticipants(String name, DateTime day);
}
