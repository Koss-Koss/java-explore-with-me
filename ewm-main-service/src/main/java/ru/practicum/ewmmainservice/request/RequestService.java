package ru.practicum.ewmmainservice.request;

import ru.practicum.ewmmainservice.request.model.dto.EventRequestStatusUpdateDto;
import ru.practicum.ewmmainservice.request.model.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewmmainservice.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(long userId, long eventId);
    ParticipationRequestDto cancel(long userId, long requestId);
    EventRequestStatusUpdateResultDto updateRequestStatus(
            long userId, long eventId, EventRequestStatusUpdateDto eventRequestStatusUpdateDto);
    List<ParticipationRequestDto> findAllByEventIdAndInitiatorId(long userId, long eventId);
    List<ParticipationRequestDto> findAllByRequesterId(long userId);

}
