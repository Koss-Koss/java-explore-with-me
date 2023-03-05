package ru.practicum.ewmmainservice.locationtype.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.locationtype.model.LocationType;

@UtilityClass
public class LocationTypeMapper {
    public static LocationTypeDto toLocationTypeDto(LocationType locationTypeStorage) {
        return LocationTypeDto.builder()
                .id(locationTypeStorage.getId())
                .name(locationTypeStorage.getName())
                .build();
    }

    public static LocationType toLocationType(LocationTypeDto locationTypeDto) {
        return LocationType.builder()
                .name(locationTypeDto.getName())
                .build();
    }
}
