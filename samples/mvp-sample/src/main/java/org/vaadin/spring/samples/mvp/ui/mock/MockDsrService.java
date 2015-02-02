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
