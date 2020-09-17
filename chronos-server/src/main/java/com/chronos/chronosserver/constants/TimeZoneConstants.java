package com.chronos.chronosserver.constants;

import com.google.common.collect.ImmutableList;

import java.util.List;

public interface TimeZoneConstants {
    List<String> VALID_TIME_ZONE_OFFSETS = ImmutableList.copyOf(new String[]{
            "-12:00",
            "-11:00",
            "-10:00",
            "-09:00",
            "-08:00",
            "-07:00",
            "-06:00",
            "-05:00",
            "-04:00",
            "-03:00",
            "-02:00",
            "+00:00",
            "+01:00",
            "+02:00",
            "+03:00",
            "+03:30",
            "+04:00",
            "+04:30",
            "+05:00",
            "+05:30",
            "+05:45",
            "+06:00",
            "+06:30",
            "+07:00",
            "+08:00",
            "+09:00",
            "+10:00",
            "+11:00",
            "+12:00"
    });
}
