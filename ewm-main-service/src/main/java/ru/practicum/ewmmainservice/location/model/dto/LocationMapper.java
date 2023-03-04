package ru.practicum.ewmmainservice.location.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.location.model.Location;

@UtilityClass
public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static LocationDto toLocationDto(Location locationStorage) {
        return LocationDto.builder()
                .lat(locationStorage.getLat())
                .lon(locationStorage.getLon())
                .build();
    }
}
