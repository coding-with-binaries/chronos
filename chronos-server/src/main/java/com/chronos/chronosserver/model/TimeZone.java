package com.chronos.chronosserver.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "time_zones")
@Entity
public class TimeZone extends BaseEntity {
    private String timeZoneName;

    private String locationName;

    private String differenceFromGmt;

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDifferenceFromGmt() {
        return differenceFromGmt;
    }

    public void setDifferenceFromGmt(String differenceFromGmt) {
        this.differenceFromGmt = differenceFromGmt;
    }
}
