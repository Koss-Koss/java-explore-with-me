package ru.practicum.ewmmainservice.request.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.request.model.Request;
import ru.practicum.ewmmainservice.request.model.RequestStatus;
import ru.practicum.ewmmainservice.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public static Request toRequest(User requester, Event event) {
        return Request.builder()
                .id(null)
                .requester(requester)
                .event(event)
                .createdOn(LocalDateTime.now())
                .status(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .createdOn(request.getCreatedOn())
                .eventId(request.getEvent().getId())
                .requesterId(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static EventRequestStatusUpdateResultDto toEventRequestStatusUpdateResultDto(
            List<ParticipationRequestDto> confirmedRequests, List<ParticipationRequestDto> rejectedRequests) {
        return EventRequestStatusUpdateResultDto.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    public static List<ParticipationRequestDto> toListParticipationRequestDto(List<Request> requests) {
        return requests
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
