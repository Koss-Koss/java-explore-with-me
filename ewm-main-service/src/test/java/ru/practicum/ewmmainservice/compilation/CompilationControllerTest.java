package ru.practicum.ewmmainservice.compilation;

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
import ru.practicum.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.practicum.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewmmainservice.compilation.model.dto.UpdateCompilationDto;
import ru.practicum.ewmmainservice.event.model.dto.EventShortDto;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.DEFAULT_PAGINATION_SORT;

@WebMvcTest(controllers = CompilationController.class)
class CompilationControllerTest {

    @MockBean
    private CompilationService compilationService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    long compilationId = 1L;
    long eventId = 1L;
    long categoryId = 1L;
    long userId = 1L;
    String title = "Тестовая подборка событий";
    LocalDateTime now = LocalDateTime.now();
    String annotation = "Аннотация тестового события";
    String eventTitle = "Заголовок тестового события";
    Boolean pinned = true;
    CategoryDto categoryDto = CategoryDto.builder().id(categoryId).name("Test category").build();
    UserShortDto userShortDto = UserShortDto.builder().id(userId).name("Test user").build();
    EventShortDto eventShortDto = EventShortDto.builder()
            .id(eventId)
            .annotation(annotation)
            .categoryDto(categoryDto)
            .confirmedRequests(0L)
            .eventDate(now.plusHours(10))
            .initiator(userShortDto)
            .paid(false)
            .title(eventTitle)
            .views(0L)
            .build();
    CompilationDto compilationDto = CompilationDto.builder()
            .id(compilationId)
            .title(title)
            .pinned(pinned)
            .events(Set.of(eventShortDto))
            .build();
    NewCompilationDto newCompilationDto = NewCompilationDto.builder()
            .title(title)
            .pinned(pinned)
            .events(Arrays.asList(eventId))
            .build();
    UpdateCompilationDto updateCompilationDto = UpdateCompilationDto.builder()
            .title(title)
            .pinned(pinned)
            .events(Arrays.asList(eventId))
            .build();
    int from = 0;
    int size = 10;
    Pageable pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT);
    Page<CompilationDto> pageCompilationDto = new PageImpl<>(
            Collections.singletonList(compilationDto), pageable, 1);

    @Test
    @DisplayName("Метод createIsAdmin - Успех")
    void createIsAdmin_whenValidCompilationDto_thenResponseStatusCreatedWithCompilationDtoInBody() throws Exception {
        when(compilationService.create(any(NewCompilationDto.class)))
                .thenReturn(compilationDto);

        mvc.perform(post(ADMIN_PATH + COMPILATION_PATH)
                        .content(mapper.writeValueAsString(newCompilationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(compilationDto)));
        verify(compilationService, times(1)).create(any(NewCompilationDto.class));
    }

    @Test
    @DisplayName("Метод createIsAdmin - Плохие входные данные")
    void createIsAdmin_whenInvalidCompilationDto_thenResponseStatusBadRequest() throws Exception {
        when(compilationService.create(any(NewCompilationDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(post(ADMIN_PATH + COMPILATION_PATH)
                        .content(mapper.writeValueAsString(newCompilationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(compilationService, times(1)).create(any(NewCompilationDto.class));
    }

    @Test
    @DisplayName("Метод createIsAdmin - Нарушены ограничения на входные данные")
    void createIsAdmin_whenRestrictionViolated_thenResponseStatusConflict() throws Exception {
        when(compilationService.create(any(NewCompilationDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(post(ADMIN_PATH + COMPILATION_PATH)
                        .content(mapper.writeValueAsString(newCompilationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(compilationService, times(1)).create(any(NewCompilationDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Успех")
    void updateIsAdmin_whenValidData_thenResponseStatusOkWithCompilationDtoInBody() throws Exception {
        when(compilationService.update(anyLong(), any(UpdateCompilationDto.class)))
                .thenReturn(compilationDto);

        mvc.perform(patch(ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .content(mapper.writeValueAsString(updateCompilationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(compilationDto)));
        verify(compilationService, times(1)).update(anyLong(), any(UpdateCompilationDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Плохие входные данные")
    void updateIsAdmin_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(compilationService.update(anyLong(), any(UpdateCompilationDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(patch(ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .content(mapper.writeValueAsString(updateCompilationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(compilationService, times(1)).update(anyLong(), any(UpdateCompilationDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Не найдена подборка событий по id")
    void updateIsAdmin_whenInvalidCompilationId_thenResponseStatusNotFound() throws Exception {
        when(compilationService.update(anyLong(), any(UpdateCompilationDto.class)))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch(ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .content(mapper.writeValueAsString(updateCompilationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(compilationService, times(1)).update(anyLong(), any(UpdateCompilationDto.class));
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - Успех")
    void deleteIsAdmin_whenValidCompilationId_thenResponseStatusNoContent() throws Exception {

        mvc.perform(delete(ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(compilationService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - Не найдена подборка событий по id")
    void deleteIsAdmin_whenInvalidCompilationId_thenResponseStatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(compilationService).delete(anyLong());

        mvc.perform(delete(ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(compilationService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод getByIdIsPublic - Успех")
    void getByIdIsPublic_whenValidCompilationId_thenResponseStatusOk() throws Exception {
        when(compilationService.findById(anyLong()))
                .thenReturn(compilationDto);

        mvc.perform(get(COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(compilationDto)));
        verify(compilationService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Метод getByIdIsPublic - Плохие входные данные")
    void getByIdIsPublic_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(compilationService.findById(anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(compilationService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Метод getByIdIsPublic - Не найдена подборка событий по id")
    void getByIdIsPublic_whenInvalidCompilationId_thenResponseStatusNotFound() throws Exception {
        when(compilationService.findById(anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(COMPILATION_PATH + COMPILATION_PREFIX, compilationId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(compilationService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Метод getAllByPinnedIsPublic - Успех")
    void getAllByPinnedIsPublic_whenValidAllParams_thenResponseStatusOkWithCompilationDtoCollectionInBody()
            throws Exception {
        when(compilationService.findByPinned(anyBoolean(), any(Pageable.class)))
                .thenReturn(pageCompilationDto);

        mvc.perform(get(COMPILATION_PATH + "?pinned={pinned}", pinned)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pageCompilationDto.getContent())));
        verify(compilationService, times(1)).findByPinned(anyBoolean(), any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getAllByPinnedIsPublic - Плохие входные данные")
    void getAllByPinnedIsPublic_whenInvalidInputData_thenResponseStatusBadRequest()
            throws Exception {
        when(compilationService.findByPinned(anyBoolean(), any(Pageable.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(COMPILATION_PATH + "?pinned={pinned}", pinned)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(compilationService, times(1)).findByPinned(anyBoolean(), any(Pageable.class));
    }
}