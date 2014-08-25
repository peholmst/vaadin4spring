package org.vaadin.spring.samples.mvp.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DSRUpdateHourlyDTO implements Serializable {
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

}
