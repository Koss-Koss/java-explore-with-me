package ru.practicum.ewmmainservice.region;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.region.model.Region;
import ru.practicum.ewmmainservice.region.model.dto.NewRegionDto;
import ru.practicum.ewmmainservice.region.model.dto.RegionDto;
import ru.practicum.ewmmainservice.region.model.dto.RegionMapper;
import ru.practicum.ewmmainservice.region.model.dto.UpdateRegionDto;
import ru.practicum.ewmmainservice.regiontype.RegionTypeRepository;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionServiceImpl implements RegionService{

    private final RegionTypeRepository regionTypeRepository;
    private final RegionRepository regionRepository;

    @Transactional
    @Override
    public RegionDto create(NewRegionDto dto) {
        RegionType regionType = regionTypeRepository.extract(dto.getRegionTypeId());
        Region createdRegion = regionRepository.save(RegionMapper.toRegion(dto, regionType));
        log.info("Добавлен регион с id = {}", createdRegion.getId());
        return RegionMapper.toRegionDto(createdRegion);
    }

    @Transactional
    @Override
    public RegionDto update(long id, UpdateRegionDto dto) {
        Region region = regionRepository.extract(id);
        RegionType regionType = null;
        if (dto.getRegionTypeId() != null) {
            regionType = regionTypeRepository.extract(dto.getRegionTypeId());
        }
        Region updatedRegion = regionRepository.save(RegionMapper.toUpdateRegion(region, dto, regionType));
        log.info("Изменён регион с id = {}", updatedRegion.getId());
        return RegionMapper.toRegionDto(updatedRegion);
    }

    @Transactional
    @Override
    public void delete(long id) {
        regionRepository.extract(id);
        regionRepository.deleteById(id);
        log.info("Удалён регион с id = {}", id);
    }

    @Override
    public Page<RegionDto> findAllByRegionType(Long regionTypeId, Pageable pageable) {
        if (regionTypeId == null) {
            return regionRepository.findAll(pageable).map(RegionMapper::toRegionDto);
        }
        RegionType regionType = regionTypeRepository.extract(regionTypeId);
        return regionRepository.findAllByRegionType(regionType, pageable).map(RegionMapper::toRegionDto);
    }

    @Override
    public RegionDto findById(long id) {
        return RegionMapper.toRegionDto(regionRepository.extract(id));
    }

    @Override
    public boolean existsRegionTypeRelatedRegions(long regionTypeId) {
        return regionRepository.findFirstByRegionType_Id(regionTypeId).isPresent();
    }
}
