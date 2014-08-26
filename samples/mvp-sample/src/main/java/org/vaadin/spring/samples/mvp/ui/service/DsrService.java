package org.vaadin.spring.samples.mvp.ui.service;

import java.util.List;

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateDTO;

public interface DsrService {

    public List<DSRUpdateDTO> getDSRHourly(DateTime day, String assetOwner) throws Exception;

}