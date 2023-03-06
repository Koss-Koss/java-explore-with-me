package ru.practicum.ewmmainservice.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.event.EventRepository;
import ru.practicum.ewmmainservice.location.model.Location;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;
import ru.practicum.ewmmainservice.location.model.dto.LocationMapper;
import ru.practicum.ewmmainservice.location.model.dto.UpdateLocationMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    @Transactional
    @Override
    public Location create(LocationDto locationDto) {
        Location createLocation = locationRepository.save(LocationMapper.toLocation(locationDto));
        log.info("Добавлена локация с id = {}", createLocation.getId());
        return createLocation;
    }

    @Transactional
    @Override
    public void update(long id, LocationDto locationDto) {
        Location location = locationRepository.extract(id);
        Location updatedLocation = UpdateLocationMapper.toUpdateLocation(location, locationDto);
        log.info("Пользователем изменено событие с id = {}", updatedLocation.getId());
        locationRepository.save(updatedLocation);
    }

    @Transactional
    @Override
    public void saveEventId(long id, long eventId) {
        locationRepository.extract(id);
        eventRepository.extract(eventId);
        log.info("Для локации с id = {} сохранён eventId = {}", id, eventId);
        locationRepository.saveEventId(id, eventId);
    }

    @Override
    public Collection<Long> findAllEventIdInRegion(double lat, double lon, double radius) {
        return locationRepository.findAllEventIdInRegion(lat, lon, radius);
    }
}
