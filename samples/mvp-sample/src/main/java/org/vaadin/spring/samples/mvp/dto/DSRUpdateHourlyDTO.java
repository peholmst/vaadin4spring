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
import java.math.BigDecimal;

public class DSRUpdateHourlyDTO implements Serializable, UniquelyKeyed<AssetOwnedHourlyLocatableId> {
    private static final long serialVersionUID = 1L;
    private final AssetOwnedHourlyLocatableId id = new AssetOwnedHourlyLocatableId();

    private String commitStatus;
    private BigDecimal economicMax;
    private BigDecimal economicMin;

    public void setCommitStatus(String commitStatus) {
        this.commitStatus = commitStatus;
    }

    public void setEconomicMax(BigDecimal economicMax) {
        this.economicMax = economicMax;
    }

    public void setEconomicMin(BigDecimal economicMin) {
        this.economicMin = economicMin;
    }

    public String getCommitStatus() {
        return commitStatus;
    }

    public BigDecimal getEconomicMax() {
        return economicMax;
    }

    public BigDecimal getEconomicMin() {
        return economicMin;
    }

    public AssetOwnedHourlyLocatableId getId() {
        return id;
    }

    @Override
    public AssetOwnedHourlyLocatableId getKey() {
        return id;
    }

}
