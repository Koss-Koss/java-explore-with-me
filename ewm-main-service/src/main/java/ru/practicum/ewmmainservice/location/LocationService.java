package ru.practicum.ewmmainservice.location;

import org.springframework.data.repository.query.Param;
import ru.practicum.ewmmainservice.location.model.Location;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;

import java.util.Collection;

public interface LocationService {

    Location create(LocationDto locationDto);

    void update(long id, LocationDto locationDto);

    void saveEventId(long id, long eventId);

    Collection<Long> findAllEventIdInRegion(double lat, double lon, double radius);
}
