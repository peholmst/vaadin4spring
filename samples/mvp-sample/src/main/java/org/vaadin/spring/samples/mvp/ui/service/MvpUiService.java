package org.vaadin.spring.samples.mvp.ui.service;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.spring.samples.mvp.dto.AssetOwnedDailyId;
import org.vaadin.spring.samples.mvp.ui.dao.ParticipantDAO;

@Service
public class MvpUiService implements UiService {

    @Inject
    private ParticipantDAO participantDao;

    @Transactional(readOnly = true)
    @Override
    public List<AssetOwnedDailyId> getParticipants(String name, DateTime day) {
        return participantDao.getParticipants(name, day);
    }

}
