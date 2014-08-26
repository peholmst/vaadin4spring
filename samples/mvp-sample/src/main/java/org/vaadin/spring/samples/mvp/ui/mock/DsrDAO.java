package org.vaadin.spring.samples.mvp.ui.mock;

import java.util.List;

import org.joda.time.DateTime;
import org.vaadin.spring.samples.mvp.dto.DSRUpdateDTO;

public interface DsrDAO {

    List<DSRUpdateDTO> getDSRHourly(DateTime day, String assetOwner);

}
