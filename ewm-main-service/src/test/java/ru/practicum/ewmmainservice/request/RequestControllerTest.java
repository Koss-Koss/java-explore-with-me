package ru.practicum.ewmmainservice.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.request.model.RequestStatus;
import ru.practicum.ewmmainservice.request.model.dto.EventRequestStatusUpdateDto;
import ru.practicum.ewmmainservice.request.model.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewmmainservice.request.model.dto.ParticipationRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    long userId = 1L;
    long eventId = 1L;
    long requestId = 1L;
    long request2Id = 2L;
    LocalDateTime now = LocalDateTime.now();
    ParticipationRequestDto participationRequestDto = ParticipationRequestDto.builder()
            .id(requestId)
            .eventId(eventId)
            .requesterId(userId)
            .status(RequestStatus.CONFIRMED)
            .createdOn(now)
            .build();
    ParticipationRequestDto participationRequest2Dto = ParticipationRequestDto.builder()
            .id(request2Id)
            .eventId(eventId)
            .requesterId(userId)
            .status(RequestStatus.REJECTED)
            .createdOn(now)
            .build();
    EventRequestStatusUpdateResultDto eventRequestStatusUpdateResultDto = EventRequestStatusUpdateResultDto.builder()
            .confirmedRequests(Arrays.asList(participationRequestDto))
            .rejectedRequests(Arrays.asList(participationRequest2Dto))
            .build();
    EventRequestStatusUpdateDto eventRequestStatusUpdateDto = EventRequestStatusUpdateDto.builder()
            .requestIds(Arrays.asList(requestId))
            .status(RequestStatus.CONFIRMED)
            .build();

    @Test
    @DisplayName("Метод createIsPrivate - Успех")
    void createIsPrivate_whenValidData_thenResponseStatusCreatedWithParticipationRequestDtoInBody() throws Exception {
        when(requestService.create(anyLong(), anyLong()))
                .thenReturn(participationRequestDto);

        mvc.perform(post(USER_PATH + USER_PREFIX + REQUEST_PATH + "?eventId={eventId}", userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(participationRequestDto)));
        verify(requestService, times(1)).create(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод createIsPrivate - Плохие входные данные")
    void createIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(requestService.create(anyLong(), anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(post(USER_PATH + USER_PREFIX + REQUEST_PATH + "?eventId={eventId}", userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, times(1)).create(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод createIsPrivate - Не найдены пользователь/событие по id")
    void createIsPrivate_whenInvalidUserIdOrEventId_thenResponseStatusNotFound() throws Exception {
        when(requestService.create(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(post(USER_PATH + USER_PREFIX + REQUEST_PATH + "?eventId={eventId}", userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(requestService, times(1)).create(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод createIsPrivate - Нарушены ограничения на входные данные")
    void createIsPrivate_whenRestrictionViolated_thenResponseStatusConflict() throws Exception {
        when(requestService.create(anyLong(), anyLong()))
                .thenThrow(ConflictException.class);

        mvc.perform(post(USER_PATH + USER_PREFIX + REQUEST_PATH + "?eventId={eventId}", userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(requestService, times(1)).create(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод cancelIsPrivate - Успех")
    void cancelIsPrivate_whenValidData_thenResponseStatusCreatedWithParticipationRequestDtoInBody() throws Exception {
        when(requestService.cancel(anyLong(), anyLong()))
                .thenReturn(participationRequestDto);

        mvc.perform(patch(USER_PATH + USER_PREFIX + REQUEST_PATH + REQUEST_PREFIX + REQUEST_CANCEL_PATH,
                        userId, requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(participationRequestDto)));
        verify(requestService, times(1)).cancel(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод cancelIsPrivate - Не найдены пользователь/запрос по id")
    void cancelIsPrivate_whenInvalidUserIdOrRequestId_thenResponseStatusNotFound() throws Exception {
        when(requestService.cancel(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + REQUEST_PATH + REQUEST_PREFIX + REQUEST_CANCEL_PATH,
                        userId, requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(requestService, times(1)).cancel(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод updateRequestsStatusIsPrivate - Успех")
    void updateRequestsStatusIsPrivate_whenValidData_thenResponseStatusOkdWithEventRequestStatusUpdateResultDtoInBody()
            throws Exception {
        when(requestService.updateRequestStatus(anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class)))
                .thenReturn(eventRequestStatusUpdateResultDto);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH,
                        userId, eventId)
                        .content(mapper.writeValueAsString(eventRequestStatusUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(eventRequestStatusUpdateResultDto)));
        verify(requestService, times(1)).updateRequestStatus(
                anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class));
    }

    @Test
    @DisplayName("Метод updateRequestsStatusIsPrivate - Плохие входные данные")
    void updateRequestsStatusIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(requestService.updateRequestStatus(anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH,
                        userId, eventId)
                        .content(mapper.writeValueAsString(eventRequestStatusUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, times(1)).updateRequestStatus(
                anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class));
    }

    @Test
    @DisplayName("Метод updateRequestsStatusIsPrivate - Не найдены пользователь/событие по id")
    void updateRequestsStatusIsPrivate_whenInvalidUserIdOrEventId_thenResponseStatusNotFound() throws Exception {
        when(requestService.updateRequestStatus(anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH,
                        userId, eventId)
                        .content(mapper.writeValueAsString(eventRequestStatusUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(requestService, times(1)).updateRequestStatus(
                anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class));
    }

    @Test
    @DisplayName("Метод updateRequestsStatusIsPrivate - Нарушены ограничения на входные данные")
    void updateRequestsStatusIsPrivate_whenRestrictionViolated_thenResponseStatusConflict() throws Exception {
        when(requestService.updateRequestStatus(anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH,
                        userId, eventId)
                        .content(mapper.writeValueAsString(eventRequestStatusUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(requestService, times(1)).updateRequestStatus(
                anyLong(), anyLong(), any(EventRequestStatusUpdateDto.class));
    }

    @Test
    @DisplayName("Метод getAllByEventIdAndInitiatorIdIsPrivate - Успех")
    void getAllByEventIdAndInitiatorIdIsPrivate_whenValidAllParams_thenResponseStatusOkWithParticipationRequestDtoListInBody()
            throws Exception {
        when(requestService.findAllByEventIdAndInitiatorId(anyLong(), anyLong()))
                .thenReturn(Arrays.asList(participationRequestDto));

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH, userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(participationRequestDto))));
        verify(requestService, times(1)).findAllByEventIdAndInitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод getAllByEventIdAndInitiatorIdIsPrivate - Плохие входные данные")
    void getAllByEventIdAndInitiatorIdIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(requestService.findAllByEventIdAndInitiatorId(anyLong(), anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH, userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, times(1)).findAllByEventIdAndInitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод getAllByEventIdAndInitiatorIdIsPrivate - Не найдены пользователь/событие по id")
    void getAllByEventIdAndInitiatorIdIsPrivate_whenInvalidUserIdOrEventId_thenResponseStatusNotFound()
            throws Exception {
        when(requestService.findAllByEventIdAndInitiatorId(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH, userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(requestService, times(1)).findAllByEventIdAndInitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод getAllByRequesterIdIsPrivate - Успех")
    void getAllByRequesterIdIsPrivate_whenValidAllParams_thenResponseStatusOkWithParticipationRequestDtoListInBody()
            throws Exception {
        when(requestService.findAllByRequesterId(anyLong()))
                .thenReturn(Arrays.asList(participationRequestDto));

        mvc.perform(get(USER_PATH + USER_PREFIX + REQUEST_PATH, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(participationRequestDto))));
        verify(requestService, times(1)).findAllByRequesterId(anyLong());
    }

    @Test
    @DisplayName("Метод getAllByRequesterIdIsPrivate - Плохие входные данные")
    void getAllByRequesterIdIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(requestService.findAllByRequesterId(anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + REQUEST_PATH, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, times(1)).findAllByRequesterId(anyLong());
    }

    @Test
    @DisplayName("Метод getAllByRequesterIdIsPrivate - Не найден пользователь по id")
    void getAllByRequesterIdIsPrivate_whenInvalidUserId_thenResponseStatusNotFound() throws Exception {
        when(requestService.findAllByRequesterId(anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + REQUEST_PATH, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(requestService, times(1)).findAllByRequesterId(anyLong());
    }
}