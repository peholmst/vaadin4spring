package org.vaadin.spring.samples.mvp.dto;

import java.io.Serializable;

/**
 * Uniquely identifies an instrument by asset owner, operating day and location
 * name
 *
 * @author cphillipson
 */
public class AssetOwnedDailyLocatableId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String location;

    private String day;

    private String assetOwner;

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getDay() {
        return day;
    }

    public void setDay(final String day) {
        this.day = day;
    }

    public String getAssetOwner() {
        return assetOwner;
    }

    public void setAssetOwner(final String assetOwner) {
        this.assetOwner = assetOwner;
    }

}

