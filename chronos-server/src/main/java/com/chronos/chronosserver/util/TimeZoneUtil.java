package com.chronos.chronosserver.util;

import com.chronos.chronosserver.dto.TimeZoneDto;
import com.chronos.chronosserver.model.TimeZone;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TimeZoneUtil {
    public static TimeZoneDto convertTimeZoneToDto(TimeZone timeZone) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(timeZone, TimeZoneDto.class);
    }

    public static TimeZone convertDtoToTimeZone(TimeZoneDto timeZoneDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(timeZoneDto, TimeZone.class);
    }

    public static List<TimeZoneDto> convertTimeZonesToDtoList(List<TimeZone> timeZones) {
        return timeZones.stream().map(TimeZoneUtil::convertTimeZoneToDto).collect(Collectors.toList());
    }

}
