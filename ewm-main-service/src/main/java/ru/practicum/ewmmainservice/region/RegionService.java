package ru.practicum.ewmmainservice.region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.region.model.dto.NewRegionDto;
import ru.practicum.ewmmainservice.region.model.dto.RegionDto;
import ru.practicum.ewmmainservice.region.model.dto.UpdateRegionDto;

public interface RegionService {

    RegionDto create(NewRegionDto dto);

    RegionDto update(long id, UpdateRegionDto dto);

    void delete(long id);

    Page<RegionDto> findAllByRegionType(Long regionTypeId, Pageable pageable);

    RegionDto findById(long id);

    boolean existsRegionTypeRelatedRegions(long regionTypeId);
}
