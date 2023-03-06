package ru.practicum.ewmmainservice.region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.region.model.Region;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;

import java.util.Optional;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.REGION_NOT_FOUND_MESSAGE;

public interface RegionRepository extends JpaRepository<Region, Long> {

    default Region extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(REGION_NOT_FOUND_MESSAGE + id));
    }

    Page<Region> findAllByRegionType(RegionType regionType, Pageable pageable);

    Optional<Region> findFirstByRegionType_Id(long regionTypeId);

}
