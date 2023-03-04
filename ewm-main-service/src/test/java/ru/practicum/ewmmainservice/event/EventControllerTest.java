package ru.practicum.ewmmainservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import ru.practicum.ewmmainservice.event.model.dto.EventFullDto;
import ru.practicum.ewmmainservice.event.model.dto.EventShortDto;
import ru.practicum.ewmmainservice.event.model.dto.NewEventDto;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.DEFAULT_PAGINATION_SORT;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureMockMvc
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
    UserDto userDto = UserDto.builder().name("Test user").email("1@1.com").build();
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
    EventShortDto eventShortDto = EventShortDto.builder()
            .id(eventId)
            .annotation(annotation)
            .categoryDto(categoryDto)
            .confirmedRequests(0L)
            .eventDate(now.plusHours(10))
            .initiator(userShortDto)
            .paid(false)
            .title(title)
            .views(0l)
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
                        //.content(mapper.writeValueAsString(newEventDto))
                        .content("{\"annotation\":\"Аннотация тестового события\",\"description\":\"Описание тестового события\",\"eventDate\":\"2023-03-05 01:36:04\",\"paid\":false,\"participantLimit\":0,\"requestModeration\":false,\"title\":\"Заголовок тестового события\",\"category\":1,\"location\":{\"lat\":55.7522,\"lon\":37.6156}}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(eventFullDto)));
        verify(eventService, times(1)).create(anyLong(), any(NewEventDto.class));
    }

    @Test
    void updateIsPrivate() {
    }

    @Test
    void updateIsAdmin() {
    }

    @Test
    void getAllByInitiatorIdIsPrivate() {
    }

    @Test
    void getByIdAndInitiatorIdIsPrivate() {
    }

    @Test
    void getAllByParamsIsAdmin() {
    }

    @Test
    void getPublishedAllByParamsAndTextIsPublic() {
    }

    @Test
    void getPublishedByIdIsPublic() {
    }
}