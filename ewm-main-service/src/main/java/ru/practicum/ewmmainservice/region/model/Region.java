package ru.practicum.ewmmainservice.region.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;

import javax.persistence.*;

@Entity
@Table(name = "regions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    @ManyToOne
    @JoinColumn(name = "region_type_id")
    RegionType regionType;
    Double lat;
    Double lon;
    Double radius;
}
