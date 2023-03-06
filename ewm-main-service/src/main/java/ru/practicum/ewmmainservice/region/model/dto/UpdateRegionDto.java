package ru.practicum.ewmmainservice.region.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.REGION_DESCRIPTION_LENGTH_MAX;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.REGION_DESCRIPTION_SIZE_INCORRECT_MESSAGE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRegionDto {
    Long id;
    String name;
    @Size(max = REGION_DESCRIPTION_LENGTH_MAX, message = REGION_DESCRIPTION_SIZE_INCORRECT_MESSAGE)
    String description;
    Double lat;
    Double lon;
    Double radius;
    @JsonProperty("region_type")
    Long regionTypeId;
}