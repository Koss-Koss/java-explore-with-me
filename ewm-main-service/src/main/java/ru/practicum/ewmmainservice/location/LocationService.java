package ru.practicum.ewmmainservice.location;

import ru.practicum.ewmmainservice.location.model.Location;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;

public interface LocationService {

    Location create(LocationDto locationDto);

    void update(long id, LocationDto locationDto);

    void saveEventId(long id, long eventId);
}
