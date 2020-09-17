package com.chronos.chronosserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TimeZoneDto extends BaseDto {
    @NotBlank(message = "Time Zone Name cannot be blank")
    private String timeZoneName;

    @NotBlank(message = "Time Zone Location cannot be blank")
    private String locationName;

    @Pattern(regexp = "^(?:Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])$", message = "Must be a valid time zone difference")
    @NotBlank(message = "Difference From GMT cannot be blank")
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
