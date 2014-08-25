package org.vaadin.spring.samples.mvp.dto;

import java.io.Serializable;

/**
 * Uniquely identifies an instrument by asset owner and operating day
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 */
public class AssetOwnedDailyId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String day;

    private String assetOwner;

    public String getDay() {
        return day;
    }

    public void setDay(final String operatingDay) {
        this.day = operatingDay;
    }

    public String getAssetOwner() {
        return assetOwner;
    }

    public void setAssetOwner(final String assetOwner) {
        this.assetOwner = assetOwner;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((assetOwner == null) ? 0 : assetOwner.hashCode());
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AssetOwnedDailyId other = (AssetOwnedDailyId) obj;
        if (assetOwner == null) {
            if (other.assetOwner != null) {
                return false;
            }
        } else if (!assetOwner.equals(other.assetOwner)) {
            return false;
        }
        if (day == null) {
            if (other.day != null) {
                return false;
            }
        } else if (!day.equals(other.day)) {
            return false;
        }
        return true;
    }

}
