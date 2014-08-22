package org.vaadin.spring.samples.mvp.dto;

import java.io.Serializable;

/**
 * Uniquely identifies an instrument by asset owner, operating hour and location
 * name
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
public class AssetOwnedHourlyLocatableId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String location;

    private String hour;

    private String assetOwner;

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(final String hour) {
        this.hour = hour;
    }

    public String getAssetOwner() {
        return assetOwner;
    }

    public void setAssetOwner(final String assetOwner) {
        this.assetOwner = assetOwner;
    }

}
