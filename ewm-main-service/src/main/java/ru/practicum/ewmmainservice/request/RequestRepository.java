package ru.practicum.ewmmainservice.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.request.model.Request;
import ru.practicum.ewmmainservice.request.model.RequestStatus;

import java.util.List;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.REQUEST_NOT_FOUND_MESSAGE;

public interface RequestRepository extends JpaRepository<Request, Long> {

    default Request extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(REQUEST_NOT_FOUND_MESSAGE + id));
    }

    int countByEventIdAndStatus(long id, RequestStatus status);

    boolean existsByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByEventIdAndStatus(long eventId, RequestStatus status);

    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByRequesterId(long requesterId);
}
