package ru.practicum.ewmmainservice.regiontype;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.regiontype.model.dto.RegionTypeDto;

public interface RegionTypeService {
    RegionTypeDto create(RegionTypeDto regionTypeDto);

    RegionTypeDto update(long id, RegionTypeDto regionTypeDto);

    void delete(long id);

    Page<RegionTypeDto> findAll(Pageable pageable);

    RegionTypeDto findById(long id);

}
