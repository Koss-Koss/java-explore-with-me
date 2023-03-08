package ru.practicum.ewmmainservice.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.location.model.Location;

import java.util.Collection;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.LOCATION_NOT_FOUND_MESSAGE;

public interface LocationRepository extends JpaRepository<Location, Long> {

    default Location extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(LOCATION_NOT_FOUND_MESSAGE + id));
    }

    @Modifying
    @Query("update Location l set l.eventId = :eventId WHERE l.id = :id")
    void saveEventId(@Param("id") long id, @Param("eventId") long eventId);

    @Query(value = "select l.eventId from Location l where distance(l.lat, l.lon, :lat, :lon) <= :radius")
    Collection<Long> findAllEventIdInRegion(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radius") double radius);
}