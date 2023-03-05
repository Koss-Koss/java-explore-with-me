package ru.practicum.ewmmainservice.locationtype;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.locationtype.model.LocationType;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.LOCATION_TYPE_NOT_FOUND_MESSAGE;

public interface LocationTypeRepository extends JpaRepository<LocationType, Long> {

    default LocationType extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(LOCATION_TYPE_NOT_FOUND_MESSAGE + id));
    }
}
