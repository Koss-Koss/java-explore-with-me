package ru.practicum.ewmmainservice.locationtype;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.locationtype.model.LocationType;
import ru.practicum.ewmmainservice.locationtype.model.dto.LocationTypeDto;
import ru.practicum.ewmmainservice.locationtype.model.dto.LocationTypeMapper;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationTypeServiceImpl implements LocationTypeService {
    private final LocationTypeRepository locationTypeRepository;
    //private final EventService eventService;

    @Transactional
    @Override
    public LocationTypeDto create(LocationTypeDto locationTypeDto) {
        try {
            LocationType createdLocationType =
                    locationTypeRepository.save(LocationTypeMapper.toLocationType(locationTypeDto));
            log.info("Добавлен тип локации с id = {}", createdLocationType.getId());
            return LocationTypeMapper.toLocationTypeDto(createdLocationType);
        } catch (DataIntegrityViolationException e) {
            log.error(LOCATION_TYPE_ALREADY_EXISTS_MESSAGE + locationTypeDto.getName());
            throw new ConflictException(LOCATION_TYPE_ALREADY_EXISTS_MESSAGE + locationTypeDto.getName());
        }
    }

    @Transactional
    @Override
    public LocationTypeDto update(long id, LocationTypeDto locationTypeDto) {
        LocationType currentLocationType = locationTypeRepository.extract(id);
        currentLocationType.setName(locationTypeDto.getName());
        try {
            LocationType updatedLocationType = locationTypeRepository.save(currentLocationType);
            log.info("Обновлен тип локации с id = {}", id);
            return LocationTypeMapper.toLocationTypeDto(updatedLocationType);
        } catch (DataIntegrityViolationException e) {
            log.error(LOCATION_TYPE_ALREADY_EXISTS_MESSAGE + locationTypeDto.getName());
            throw new ConflictException(LOCATION_TYPE_ALREADY_EXISTS_MESSAGE + locationTypeDto.getName());
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        locationTypeRepository.extract(id);
        /*if (eventService.existsCategoryRelatedEvents(id)) {
            throw new ConflictException(DELETE_CATEGORY_WITH_RELATED_EVENTS_MESSAGE);
        }*/
        locationTypeRepository.deleteById(id);
        log.info("Удален тип локации с id = {}", id);
    }

    @Override
    public Page<LocationTypeDto> findAll(Pageable pageable) {
        return locationTypeRepository.findAll(pageable).map(LocationTypeMapper::toLocationTypeDto);
    }

    @Override
    public LocationTypeDto findById(long id) {
        return LocationTypeMapper.toLocationTypeDto(locationTypeRepository.extract(id));
    }
}
