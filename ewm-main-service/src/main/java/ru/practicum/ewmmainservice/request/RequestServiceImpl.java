package ru.practicum.ewmmainservice.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.event.EventRepository;
import ru.practicum.ewmmainservice.event.EventService;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.request.model.Request;
import ru.practicum.ewmmainservice.request.model.RequestStatus;
import ru.practicum.ewmmainservice.request.model.dto.*;
import ru.practicum.ewmmainservice.user.UserRepository;
import ru.practicum.ewmmainservice.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public ParticipationRequestDto create(long userId, long eventId) {
        User requester = userRepository.extract(userId);
        Event event = eventRepository.extract(eventId);
        int numberRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException(ADDING_REPEAT_REQUEST_MESSAGE);
        }

        if (event.getInitiator().getId().equals(userId)) {
            log.error(REQUESTER_EQUALS_INITIATOR_MESSAGE);
            throw new ConflictException(REQUESTER_EQUALS_INITIATOR_MESSAGE);
        }

        if (event.getState().equals(EventState.PENDING)) {
            log.error(PARTICIPATION_IN_PENDING_EVENT_MESSAGE);
            throw new ConflictException(PARTICIPATION_IN_PENDING_EVENT_MESSAGE);
        }

        if (numberRequests >= event.getParticipantLimit()) {
            log.error(REQUEST_LIMIT_REACHED_MESSAGE + eventId);
            throw new ConflictException(REQUEST_LIMIT_REACHED_MESSAGE + eventId);
        }

        Request createdRequest = requestRepository.save(RequestMapper.toRequest(requester, event));
        log.info("Для участия в событии добавлен запрос с id = {}", createdRequest.getId());
        return RequestMapper.toParticipationRequestDto(createdRequest);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        userRepository.extract(userId);
        Request request = requestRepository.extract(requestId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            eventService.changeConfirmedRequests(request.getEvent().getId(), -1);
        }
        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResultDto updateRequestStatus(
            long userId, long eventId, EventRequestStatusUpdateDto eventRequestStatusUpdateDto) {
        List<Long> requestIds =
                eventRequestStatusUpdateDto != null ? eventRequestStatusUpdateDto.getRequestIds() : new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        userRepository.extract(userId);
        Event event = eventRepository.extract(eventId);

        if (requestIds.isEmpty()) {
            return RequestMapper.toEventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
        }

        if (!event.getRequestModeration()) {
            for (Long id : requestIds) {
                Request request = requestRepository.extract(id);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(request));
            }
            return RequestMapper.toEventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
        }

        if (eventRequestStatusUpdateDto.getStatus().equals(RequestStatus.REJECTED)) {
            for (Long id : requestIds) {
                Request request = requestRepository.extract(id);
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new ConflictException(REJECT_ALREADY_CONFIRMED_REQUEST_MESSAGE + id);
                }
                if (!request.getStatus().equals(RequestStatus.PENDING)) {
                    throw new ConflictException(UPDATE_REQUEST_WITHOUT_PENDING_STATUS_MESSAGE + id);
                }
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                rejectedRequests.add(RequestMapper.toParticipationRequestDto(request));
            }
            return RequestMapper.toEventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
        }

        for (Long id : requestIds) {
            Request request = requestRepository.extract(id);

            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException(UPDATE_REQUEST_WITHOUT_PENDING_STATUS_MESSAGE + id);
            }

            if (event.getParticipantLimit() == 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                requestRepository.save(request);
                eventService.changeConfirmedRequests(eventId, 1);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(request));
            } else if (requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED) >=
                    event.getParticipantLimit()) {
                throw new ConflictException(REACHED_LIMIT_OF_PARTICIPANTS_MESSAGE + eventId);
            } else {
                request.setStatus(RequestStatus.CONFIRMED);
                requestRepository.save(request);
                eventService.changeConfirmedRequests(eventId, 1);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(request));
            }
        }
        return RequestMapper.toEventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<ParticipationRequestDto> findAllByEventIdAndInitiatorId(long userId, long eventId) {
        User initiator = userRepository.extract(userId);
        Event event = eventRepository.extract(eventId);
        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new BadRequestException(USER_GET_NOT_EVENT_INITIATOR_MESSAGE + eventId);
        }
        List<Request> requests = requestRepository.findAllByEventId(event.getId());
        return RequestMapper.toListParticipationRequestDto(requests);
    }

    @Override
    public List<ParticipationRequestDto> findAllByRequesterId(long userId) {
        userRepository.extract(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.toListParticipationRequestDto(requests);
    }

}
