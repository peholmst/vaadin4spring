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
package org.vaadin.spring.samples.mvp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DSRUpdateDTO implements Serializable, UniquelyKeyed<AssetOwnedDailyLocatableId> {

    private static final long serialVersionUID = 1L;
    private final AssetOwnedDailyLocatableId id = new AssetOwnedDailyLocatableId();

    private List<DSRUpdateHourlyDTO> records = new ArrayList<>(25);

    public List<DSRUpdateHourlyDTO> getRecords() { return records; }
    public void setRecords(List<DSRUpdateHourlyDTO> records) { this.records = records; }

    public AssetOwnedDailyLocatableId getId() { return id; }

    @Override
    public AssetOwnedDailyLocatableId getKey() {
        return id;
    }
}

