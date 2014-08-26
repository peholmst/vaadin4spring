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

