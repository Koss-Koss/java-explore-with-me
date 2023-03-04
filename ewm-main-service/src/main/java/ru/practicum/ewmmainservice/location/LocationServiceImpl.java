package ru.practicum.ewmmainservice.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.location.model.Location;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;
import ru.practicum.ewmmainservice.location.model.dto.LocationMapper;
import ru.practicum.ewmmainservice.location.model.dto.UpdateLocationMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl  implements LocationService {
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
    };
}
