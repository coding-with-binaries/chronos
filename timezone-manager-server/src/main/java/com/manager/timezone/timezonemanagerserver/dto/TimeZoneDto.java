package com.manager.timezone.timezonemanagerserver.dto;

public class TimeZoneDto {
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
