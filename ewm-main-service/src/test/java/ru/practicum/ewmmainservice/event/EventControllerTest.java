package ru.practicum.ewmmainservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.event.model.dto.*;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.DEFAULT_PAGINATION_SORT;

@WebMvcTest(controllers = EventController.class)
class EventControllerTest {

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    long userId = 1L;
    long categoryId = 1L;
    long eventId = 1L;
    LocalDateTime now = LocalDateTime.now();
    String annotation = "Аннотация тестового события";
    String description = "Описание тестового события";
    String title = "Заголовок тестового события";
    CategoryDto categoryDto = CategoryDto.builder().id(categoryId).name("Test category").build();
    LocationDto locationDto = LocationDto.builder().lat(55.7522).lon(37.6156).build();
    UserShortDto userShortDto = UserShortDto.builder().id(userId).name("Test user").build();
    NewEventDto newEventDto = NewEventDto.builder()
            .annotation(annotation)
            .categoryId(categoryId)
            .description(description)
            .eventDate(now.plusHours(10))
            .locationDto(locationDto)
            .paid(false)
            .participantLimit(0L)
            .requestModeration(false)
            .title(title)
            .build();
    UpdateUserEventDto updateUserEventDto = UpdateUserEventDto.builder()
            .annotation(annotation)
            .categoryId(categoryId)
            .description(description)
            .eventDate(now.plusHours(10))
            .locationDto(locationDto)
            .paid(false)
            .participantLimit(0L)
            .requestModeration(false)
            .state(EventState.PUBLISHED)
            .title(title)
            .build();
    UpdateAdminEventDto updateAdminEventDto = UpdateAdminEventDto.builder()
            .annotation(annotation)
            .categoryId(categoryId)
            .description(description)
            .eventDate(now.plusHours(10))
            .locationDto(locationDto)
            .paid(false)
            .participantLimit(0L)
            .requestModeration(false)
            .state(EventState.PUBLISHED)
            .title(title)
            .build();
    EventShortDto eventShortDto = EventShortDto.builder()
            .id(eventId)
            .annotation(annotation)
            .categoryDto(categoryDto)
            .confirmedRequests(0L)
            .eventDate(now.plusHours(10))
            .initiator(userShortDto)
            .paid(false)
            .title(title)
            .views(0L)
            .build();
    EventFullDto eventFullDto = EventFullDto.builder()
            .id(eventId)
            .annotation(annotation)
            .categoryDto(categoryDto)
            .confirmedRequests(0L)
            .createdOn(now)
            .description(description)
            .eventDate(now.plusHours(10))
            .initiator(userShortDto)
            .locationDto(locationDto)
            .paid(false)
            .participantLimit(0L)
            .publishedOn(now.plusHours(5))
            .requestModeration(false)
            .state(EventState.PUBLISHED)
            .title(title)
            .views(0L)
            .build();
    String users = "1";
    String states = "PUBLISHED";
    String categories = "1";
    String rangeStart= "2022-01-06 13:30:38";
    String rangeEnd= "2097-01-06 13:30:38";
    String text= "Test text";
    String paid= "true";
    String onlyAvailable = "true";
    int from = 0;
    int size = 10;
    Pageable pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT);
    Page<EventShortDto> pageEventShortDto = new PageImpl<>(
            Collections.singletonList(eventShortDto), pageable, 1);
    Page<EventFullDto> pageEventFullDto = new PageImpl<>(
            Collections.singletonList(eventFullDto), pageable, 1);

    @Test
    @DisplayName("Метод createIsPrivate - Успех")
    void createIsPrivate_whenValidData_thenResponseStatusCreatedWithEventFullDtoInBody() throws Exception {
        when(eventService.create(anyLong(), any(NewEventDto.class)))
                .thenReturn(eventFullDto);

        mvc.perform(post(USER_PATH + USER_PREFIX + EVENT_PATH, userId)
                        .content(newEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(eventFullDto)));
        verify(eventService, times(1)).create(anyLong(), any(NewEventDto.class));
    }

    @Test
    @DisplayName("Метод createIsPrivate - Плохие входные данные")
    void createIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(eventService.create(anyLong(), any(NewEventDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(post(USER_PATH + USER_PREFIX + EVENT_PATH, userId)
                        .content(newEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).create(anyLong(), any(NewEventDto.class));
    }

    @Test
    @DisplayName("Метод createIsPrivate - Нарушены ограничения на входные данные")
    void createIsPrivate_whenRestrictionViolated_thenResponseStatusConflict() throws Exception {
        when(eventService.create(anyLong(), any(NewEventDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(post(USER_PATH + USER_PREFIX + EVENT_PATH, userId)
                        .content(newEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(eventService, times(1)).create(anyLong(), any(NewEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsPrivate - Успех")
    void updateIsPrivate_whenValidData_thenResponseStatusOkdWithEventFullDtoInBody() throws Exception {
        when(eventService.updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class)))
                .thenReturn(eventFullDto);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .content(updateUserEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(eventFullDto)));
        verify(eventService, times(1)).updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsPrivate - Плохие входные данные")
    void updateIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(eventService.updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .content(updateUserEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsPrivate - Не найдены пользователь/событие по id")
    void updateIsPrivate_whenInvalidUserIdOrEventId_thenResponseStatusNotFound() throws Exception {
        when(eventService.updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .content(updateUserEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(eventService, times(1)).updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsPrivate - Нарушены ограничения на входные данные")
    void updateIsPrivate_whenRestrictionViolated_thenResponseStatusConflict() throws Exception {
        when(eventService.updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(patch(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .content(updateUserEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(eventService, times(1)).updateIsPrivate(anyLong(),anyLong(), any(UpdateUserEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Успех")
    void updateIsAdmin_whenValidData_thenResponseStatusCreatedWithEventFullDtoInBody() throws Exception {
        when(eventService.updateIsAdmin(anyLong(), any(UpdateAdminEventDto.class)))
                .thenReturn(eventFullDto);

        mvc.perform(patch(ADMIN_PATH + EVENT_PATH + EVENT_PREFIX, eventId)
                        .content(updateAdminEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(eventFullDto)));
        verify(eventService, times(1)).updateIsAdmin(anyLong(), any(UpdateAdminEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Не найдено событие по id")
    void updateIsAdmin_whenInvalidEventId_thenResponseStatusNotFound() throws Exception {
        when(eventService.updateIsAdmin(anyLong(), any(UpdateAdminEventDto.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch(ADMIN_PATH + EVENT_PATH + EVENT_PREFIX, eventId)
                        .content(updateAdminEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(eventService, times(1)).updateIsAdmin(anyLong(), any(UpdateAdminEventDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Нарушены ограничения на входные данные")
    void updateIsAdmin_whenRestrictionViolated_thenResponseStatusConflict() throws Exception {
        when(eventService.updateIsAdmin(anyLong(), any(UpdateAdminEventDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(patch(ADMIN_PATH + EVENT_PATH + EVENT_PREFIX, eventId)
                        .content(updateAdminEventDto.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(eventService, times(1)).updateIsAdmin(anyLong(), any(UpdateAdminEventDto.class));
    }

    @Test
    @DisplayName("Метод getAllByInitiatorIdIsPrivate - Успех")
    void getAllByInitiatorIdIsPrivate_whenValidAllParams_thenResponseStatusOkWithEventShortDtoCollectionInBody()
            throws Exception {
        when(eventService.findAllByInitiatorId(anyLong(), any(Pageable.class)))
                .thenReturn(pageEventShortDto);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pageEventShortDto.getContent())));
        verify(eventService, times(1)).findAllByInitiatorId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getAllByInitiatorIdIsPrivate - Плохие входные данные")
    void getAllByInitiatorIdIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(eventService.findAllByInitiatorId(anyLong(), any(Pageable.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).findAllByInitiatorId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getByIdAndInitiatorIdIsPrivate - Успех")
    void getByIdAndInitiatorIdIsPrivate_whenValidAllParams_thenResponseStatusOkWithEventFullDtoInBody()
            throws Exception {
        when(eventService.findByIdAndInitiatorId(anyLong(), anyLong()))
                .thenReturn(eventFullDto);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(eventFullDto)));
        verify(eventService, times(1)).findByIdAndInitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод getByIdAndInitiatorIdIsPrivate - Плохие входные данные")
    void getByIdAndInitiatorIdIsPrivate_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(eventService.findByIdAndInitiatorId(anyLong(), anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).findByIdAndInitiatorId(anyLong(), anyLong());
    }


    @Test
    @DisplayName("Метод getByIdAndInitiatorIdIsPrivate - Не найдены пользователь/событие по id")
    void getByIdAndInitiatorIdIsPrivate_whenInvalidUserIdOrEventId_thenResponseStatusNotFound() throws Exception {
        when(eventService.findByIdAndInitiatorId(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX, userId, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(eventService, times(1)).findByIdAndInitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Метод getAllByParamsIsAdmin - Успех")
    void getAllByParamsIsAdmin_whenValidAllParams_thenResponseStatusOkWithEventFullDtoCollectionInBody()
            throws Exception {
        when(eventService.findAllByParams(
                anySet(), anySet(), anySet(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(pageEventFullDto);

        mvc.perform(get(ADMIN_PATH + EVENT_PATH +
                                "?users={users}&states={states}&categories={categories}&rangeStart={rangeStart}" +
                                "&rangeEnd={rangeEnd}&from={from}&size={size}",
                        users, states, categories, rangeStart, rangeEnd, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pageEventFullDto.getContent())));
        verify(eventService, times(1)).findAllByParams(
                anySet(), anySet(), anySet(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getAllByParamsIsAdmin - Плохие входные данные")
    void getAllByParamsIsAdmin_whenInvalidInputData_thenResponseStatusBadRequest()
            throws Exception {
        when(eventService.findAllByParams(
                anySet(), anySet(), anySet(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(ADMIN_PATH + EVENT_PATH +
                                "?users={users}&states={states}&categories={categories}&rangeStart={rangeStart}" +
                                "&rangeEnd={rangeEnd}&from={from}&size={size}",
                        users, states, categories, rangeStart, rangeEnd, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).findAllByParams(
                anySet(), anySet(), anySet(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getPublishedAllByParamsAndTextIsPublic - Успех")
    void getPublishedAllByParamsAndTextIsPublic_whenValidAllParams_thenResponseStatusOkWithEventShortDtoCollectionInBody()
            throws Exception {
        when(eventService.findPublishedAllByParamsAndText(
                anyString(), anySet(), anyBoolean(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyBoolean(), any(Pageable.class), any(HttpServletRequest.class)))
                .thenReturn(pageEventShortDto);

        mvc.perform(get(EVENT_PATH + "?text={text}&categories={categories}&paid={paid}&rangeStart={rangeStart}" +
                                "&rangeEnd={rangeEnd}&onlyAvailable={onlyAvailable}&from={from}&size={size}",
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pageEventShortDto.getContent())));
        verify(eventService, times(1)).findPublishedAllByParamsAndText(
                anyString(), anySet(), anyBoolean(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyBoolean(), any(Pageable.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Метод getPublishedAllByParamsAndTextIsPublic - Плохие входные данные")
    void getPublishedAllByParamsAndTextIsPublic_whenInvalidInputData_thenResponseStatusBadRequest()
            throws Exception {
        when(eventService.findPublishedAllByParamsAndText(
                anyString(), anySet(), anyBoolean(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyBoolean(), any(Pageable.class), any(HttpServletRequest.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(EVENT_PATH + "?text={text}&categories={categories}&paid={paid}&rangeStart={rangeStart}" +
                                "&rangeEnd={rangeEnd}&onlyAvailable={onlyAvailable}&from={from}&size={size}",
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).findPublishedAllByParamsAndText(
                anyString(), anySet(), anyBoolean(), any(LocalDateTime.class),
                any(LocalDateTime.class), anyBoolean(), any(Pageable.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Метод getPublishedByIdIsPublic - Успех")
    void getPublishedByIdIsPublic_whenValidAllParams_thenResponseStatusOkWithEventFullDtoInBody()
            throws Exception {
        when(eventService.findPublishedById(anyLong(), any(HttpServletRequest.class)))
                .thenReturn(eventFullDto);

        mvc.perform(get(EVENT_PATH + EVENT_PREFIX, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(eventFullDto)));
        verify(eventService, times(1)).findPublishedById(anyLong(), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Метод getPublishedByIdIsPublic - Плохие входные данные")
    void getPublishedByIdIsPublic_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(eventService.findPublishedById(anyLong(), any(HttpServletRequest.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(EVENT_PATH + EVENT_PREFIX, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(eventService, times(1)).findPublishedById(anyLong(), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("Метод getPublishedByIdIsPublic - Не найдено событие по id")
    void getPublishedByIdIsPublic_whenInvalidUserId_thenResponseStatusNotFound() throws Exception {
        when(eventService.findPublishedById(anyLong(), any(HttpServletRequest.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(EVENT_PATH + EVENT_PREFIX, eventId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(eventService, times(1)).findPublishedById(anyLong(), any(HttpServletRequest.class));
    }
}