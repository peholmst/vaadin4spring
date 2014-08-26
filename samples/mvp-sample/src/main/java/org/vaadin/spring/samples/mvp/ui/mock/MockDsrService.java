package org.vaadin.spring.samples.mvp.ui.mock;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateDTO;
import org.vaadin.spring.samples.mvp.ui.service.DsrService;

@Service
public class MockDsrService implements DsrService {

    @Inject
    DsrDAO dao;

    @Transactional(readOnly = true)
    @Override
    public List<DSRUpdateDTO> getDSRHourly(DateTime day, String assetOwner)
            throws Exception {
        return dao.getDSRHourly(day, assetOwner);
    }

}
