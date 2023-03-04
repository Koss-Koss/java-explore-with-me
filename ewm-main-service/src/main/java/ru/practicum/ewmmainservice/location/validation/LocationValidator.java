package ru.practicum.ewmmainservice.location.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@UtilityClass
public class LocationValidator {
    public static void validate(LocationDto locationDto) {
        Double lat = locationDto.getLat();
        if (lat == null) {
            throw new ConflictException(NULL_LOCATION_LATITUDE_MESSAGE);
        }
        if (lat < -90 || lat > 90) {
            throw new ConflictException(INCORRECT_LATITUDE_VALUE_MESSAGE);
        }

        Double lon = locationDto.getLon();
        if (lon == null) {
            throw new ConflictException(NULL_LOCATION_LONGITUDE_MESSAGE);
        }
        if (lon < 0 || lon > 180) {
            throw new ConflictException(INCORRECT_LONGITUDE_VALUE_MESSAGE);
        }
    }
}
