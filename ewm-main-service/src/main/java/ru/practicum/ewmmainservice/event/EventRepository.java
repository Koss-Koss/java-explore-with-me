package ru.practicum.ewmmainservice.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.EVENT_NOT_FOUND_MESSAGE;

public interface EventRepository extends JpaRepository<Event, Long> {

    default Event extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(EVENT_NOT_FOUND_MESSAGE + id));
    }

    Optional<Event> findFirstByCategory_Id(long categoryId);

    Page<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    @Query(value = "select e from Event e where " +
            "(:users is null or e.initiator.id in :users) and " +
            "(:states is null or e.state in :states) and " +
            "(:categories is null or e.category.id in :categories) and " +
            "(cast(:rangeStart as date) is null or e.eventDate >= :rangeStart) and " +
            "(cast(:rangeEnd as date) is null or e.eventDate <= :rangeEnd)")
    Page<Event> findAllByParams(
            @Param("users") Set<Long> users,
            @Param("states") Set<EventState> states,
            @Param("categories") Set<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable page);

    @Query(value = "select e from Event e where " +
            "e.state = ru.practicum.ewmmainservice.event.model.EventState.PUBLISHED and " +
            "(lower(e.annotation) like lower(concat('%', :text, '%')) or " +
            "lower(e.description) like lower(concat('%', :text, '%')) ) and " +
            "(:categories is null or e.category.id in :categories) and " +
            "(:paid is null or e.paid = :paid) and " +
            "(cast(:rangeStart as date) is null or e.eventDate >= :rangeStart) and " +
            "(cast(:rangeEnd as date) is null or e.eventDate <= :rangeEnd) and " +
            "(:onlyAvailable is false or :onlyAvailable is true and e.confirmedRequests < e.participantLimit)"
    )
    Page<Event> findPublishedAllByParamsAndText(
            @Param("text") String text,
            @Param("categories") Set<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable page);

    @Modifying
    @Query("update Event e set e.confirmedRequests = e.confirmedRequests + :changeSize WHERE e.id = :id")
    void changeConfirmedRequests(@Param("id") long id, @Param("changeSize") long changeSize);

    @Modifying
    @Query("update Event e set e.views = e.views + 1 WHERE e.id in :ids")
    void changeViews(@Param("ids") Set<Long> ids);
}
