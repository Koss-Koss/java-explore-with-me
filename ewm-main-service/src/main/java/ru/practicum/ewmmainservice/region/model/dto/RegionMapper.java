package ru.practicum.ewmmainservice.region.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.event.model.dto.UpdateUserEventDto;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.region.model.Region;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;
import ru.practicum.ewmmainservice.regiontype.model.dto.RegionTypeMapper;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.NOT_CANCELED_EVENT_INITIATOR_MESSAGE;

@UtilityClass
public class RegionMapper {
    public static Region toRegion(NewRegionDto newRegionDto, RegionType regionType) {
        return Region.builder()
                .id(null)
                .name(newRegionDto.getName())
                .description(newRegionDto.getDescription())
                .lat(newRegionDto.getLat())
                .lon(newRegionDto.getLon())
                .radius(newRegionDto.getRadius())
                .regionType(regionType)
                .build();
    }

    public static RegionDto toRegionDto(Region regionStorage) {
        return RegionDto.builder()
                .id(regionStorage.getId())
                .name(regionStorage.getName())
                .description(regionStorage.getDescription())
                .lat(regionStorage.getLat())
                .lon(regionStorage.getLon())
                .radius(regionStorage.getRadius())
                .regionTypeDto(RegionTypeMapper.toRegionTypeDto(regionStorage.getRegionType()))
                .build();
    }

    public static Region toUpdateRegion(Region region,  UpdateRegionDto dto, RegionType regionType) {

        if (dto.getName() != null) {
            region.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            region.setDescription(dto.getDescription());
        }
        if (dto.getLat() != null) {
            region.setLat(dto.getLat());
        }
        if (dto.getLon() != null) {
            region.setLon(dto.getLon());
        }
        if (dto.getRadius() != null) {
            region.setRadius(dto.getRadius());
        }
        if (dto.getRadius() != null) {
            region.setRadius(dto.getRadius());
        }
        if (regionType != null && !region.getRegionType().equals(regionType)) {
            region.setRegionType(regionType);
        }
        return region;
    }
}
