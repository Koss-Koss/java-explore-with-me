package ru.practicum.ewmmainservice.regiontype;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.regiontype.model.RegionType;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.REGION_TYPE_NOT_FOUND_MESSAGE;

public interface RegionTypeRepository extends JpaRepository<RegionType, Long> {

    default RegionType extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(REGION_TYPE_NOT_FOUND_MESSAGE + id));
    }
}
