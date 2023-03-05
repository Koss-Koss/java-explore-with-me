package ru.practicum.ewmmainservice.user;

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
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.DEFAULT_PAGINATION_SORT;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    long userId = 1L;
    UserDto userDto = UserDto.builder().name("Test user").email("1@1.com").build();
    int from = 0;
    int size = 10;
    Pageable pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT);
    Page<UserDto> pageUserDto = new PageImpl<>(
            Collections.singletonList(userDto), pageable, 1);

    @Test
    @DisplayName("Метод createIsAdmin - Успех")
    void createIsAdmin_whenValidAllParams_thenResponseStatusCreatedWithUserDtoInBody() throws Exception {
        when(userService.create(any(UserDto.class)))
                .thenReturn(userDto);

        mvc.perform(post(ADMIN_PATH + USER_PATH)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    @DisplayName("Метод createIsAdmin - Плохие входные данные")
    void createIsAdmin_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(userService.create(any(UserDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(post(ADMIN_PATH + USER_PATH)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    @DisplayName("Метод createIsAdmin - Не уникальный email пользователя")
    void createIsAdmin_whenNotUniqueUserEmail_thenResponseStatusConflict() throws Exception {
        when(userService.create(any(UserDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(post(ADMIN_PATH + USER_PATH)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - Успех")
    void deleteIsAdmin_whenValidUserId_thenResponseStatusOk() throws Exception {
        mvc.perform(delete(ADMIN_PATH + USER_PATH + USER_PREFIX, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - Не найден пользователь по id")
    void deleteIsAdmin_whenInvalidCategoryId_thenResponseStatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(userService).delete(anyLong());

        mvc.perform(delete(ADMIN_PATH + USER_PATH + USER_PREFIX, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод getAllIsAdmin - Успех")
    void getAllIsAdmin_whenValidAllParams_thenResponseStatusOkWithUserDtoCollectionInBody() throws Exception {
        when(userService.findAllByIds(anyCollection(), any(Pageable.class)))
                .thenReturn(pageUserDto);

        mvc.perform(get(ADMIN_PATH + USER_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pageUserDto.getContent())));
        verify(userService, times(1)).findAllByIds(anyCollection(), any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getAllIsAdmin - Плохие входные данные")
    void getAllIsAdmin_whenInvalidInputData_thenResponseStatusBadRequest() throws Exception {
        when(userService.findAllByIds(anyCollection(), any(Pageable.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(ADMIN_PATH + USER_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).findAllByIds(anyCollection(), any(Pageable.class));
    }
}