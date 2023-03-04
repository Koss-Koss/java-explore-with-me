package ru.practicum.ewmmainservice.location.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.location.model.Location;

@UtilityClass
public class UpdateLocationMapper {

    public static Location toUpdateLocation(Location location, LocationDto dto) {
        if (dto.getLat() != null) {
            location.setLat(dto.getLat());
        }
        if (dto.getLon() != null) {
            location.setLon(dto.getLon());
        }
        return location;
    }
}
