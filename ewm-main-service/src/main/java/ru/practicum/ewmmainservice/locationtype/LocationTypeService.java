package ru.practicum.ewmmainservice.locationtype;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.locationtype.model.dto.LocationTypeDto;

public interface LocationTypeService {
    LocationTypeDto create(LocationTypeDto locationTypeDto);

    LocationTypeDto update(long id, LocationTypeDto locationTypeDto);

    void delete(long id);

    Page<LocationTypeDto> findAll(Pageable pageable);

    LocationTypeDto findById(long id);
}
