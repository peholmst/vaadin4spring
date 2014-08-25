package org.vaadin.spring.samples.mvp.ui.service;

import java.util.List;

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;

public interface UiService {

    List<AssetOwnedDailyId> getParticipants(String name, DateTime day);
}
