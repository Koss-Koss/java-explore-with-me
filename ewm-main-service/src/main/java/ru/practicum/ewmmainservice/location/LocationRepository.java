package ru.practicum.ewmmainservice.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.location.model.Location;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.LOCATION_NOT_FOUND_MESSAGE;

public interface LocationRepository extends JpaRepository<Location, Long> {

    default Location extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(LOCATION_NOT_FOUND_MESSAGE + id));
    }
}