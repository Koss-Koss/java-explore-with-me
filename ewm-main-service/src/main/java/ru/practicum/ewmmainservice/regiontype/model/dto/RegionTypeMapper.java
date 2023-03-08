package ru.practicum.ewmmainservice.regiontype.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;

@UtilityClass
public class RegionTypeMapper {
    public static RegionTypeDto toRegionTypeDto(RegionType regionTypeStorage) {
        return RegionTypeDto.builder()
                .id(regionTypeStorage.getId())
                .name(regionTypeStorage.getName())
                .build();
    }

    public static RegionType toRegionType(RegionTypeDto regionTypeDto) {
        return RegionType.builder()
                .name(regionTypeDto.getName())
                .build();
    }
}
