package ru.practicum.ewmmainservice.region.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.regiontype.model.dto.RegionTypeDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegionDto {
    Long id;
    String name;
    String description;
    Double lat;
    Double lon;
    Double radius;
    @JsonProperty("region_type")
    RegionTypeDto regionTypeDto;
}
