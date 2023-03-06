package ru.practicum.ewmmainservice.region.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.exception.ConflictException;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.INCORRECT_LONGITUDE_VALUE_MESSAGE;

@UtilityClass
public class RegionValidator {
    public static void validate(Double lat, Double lon, Double radius, String method) {
        if (lat == null && method == "POST") {
            throw new ConflictException(NULL_REGION_LATITUDE_MESSAGE);
        }
        if (lat != null && (lat < -90 || lat > 90)) {
            throw new ConflictException(INCORRECT_LATITUDE_VALUE_MESSAGE);
        }

        if (lon == null && method == "POST") {
            throw new ConflictException(NULL_REGION_LONGITUDE_MESSAGE);
        }
        if (lon != null && (lon < 0 || lon > 180)) {
            throw new ConflictException(INCORRECT_LONGITUDE_VALUE_MESSAGE);
        }

        if (radius == null && method == "POST") {
            throw new ConflictException(NULL_REGION_RADIUS_MESSAGE);
        }
        if (radius != null && (radius < 0 || radius > 20000)) {
            throw new ConflictException(INCORRECT_RADIUS_VALUE_MESSAGE);
        }
    }
}
