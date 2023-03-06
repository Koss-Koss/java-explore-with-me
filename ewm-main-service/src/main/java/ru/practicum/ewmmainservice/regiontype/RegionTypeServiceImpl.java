package ru.practicum.ewmmainservice.regiontype;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.region.RegionService;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;
import ru.practicum.ewmmainservice.regiontype.model.dto.RegionTypeDto;
import ru.practicum.ewmmainservice.regiontype.model.dto.RegionTypeMapper;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionTypeServiceImpl implements RegionTypeService {
    private final RegionTypeRepository regionTypeRepository;
    private final RegionService regionService;

    @Transactional
    @Override
    public RegionTypeDto create(RegionTypeDto regionTypeDto) {
        try {
            RegionType createdRegionType =
                    regionTypeRepository.save(RegionTypeMapper.toRegionType(regionTypeDto));
            log.info("Добавлен тип региона с id = {}", createdRegionType.getId());
            return RegionTypeMapper.toRegionTypeDto(createdRegionType);
        } catch (DataIntegrityViolationException e) {
            log.error(REGION_TYPE_ALREADY_EXISTS_MESSAGE + regionTypeDto.getName());
            throw new ConflictException(REGION_TYPE_ALREADY_EXISTS_MESSAGE + regionTypeDto.getName());
        }
    }

    @Transactional
    @Override
    public RegionTypeDto update(long id, RegionTypeDto regionTypeDto) {
        RegionType currentRegionType = regionTypeRepository.extract(id);
        currentRegionType.setName(regionTypeDto.getName());
        try {
            RegionType updatedRegionType = regionTypeRepository.save(currentRegionType);
            log.info("Обновлен тип региона с id = {}", id);
            return RegionTypeMapper.toRegionTypeDto(updatedRegionType);
        } catch (DataIntegrityViolationException e) {
            log.error(REGION_TYPE_ALREADY_EXISTS_MESSAGE + regionTypeDto.getName());
            throw new ConflictException(REGION_TYPE_ALREADY_EXISTS_MESSAGE + regionTypeDto.getName());
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        regionTypeRepository.extract(id);
        if (regionService.existsRegionTypeRelatedRegions(id)) {
            throw new ConflictException(DELETE_REGION_TYPE_WITH_RELATED_REGIONS_MESSAGE);
        }
        regionTypeRepository.deleteById(id);
        log.info("Удален тип региона с id = {}", id);
    }

    @Override
    public Page<RegionTypeDto> findAll(Pageable pageable) {
        return regionTypeRepository.findAll(pageable).map(RegionTypeMapper::toRegionTypeDto);
    }

    @Override
    public RegionTypeDto findById(long id) {
        return RegionTypeMapper.toRegionTypeDto(regionTypeRepository.extract(id));
    }
}
