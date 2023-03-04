package ru.practicum.ewmmainservice.category;

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
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.DEFAULT_PAGINATION_SORT;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    long categoryId = 1L;
    CategoryDto categoryDto = CategoryDto.builder().name("Test category").build();
    int from = 0;
    int size = 10;
    Pageable pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT);
    Page<CategoryDto> pageCategoryDto = new PageImpl<>(
            Collections.singletonList(categoryDto), pageable, 1);

    @Test
    @DisplayName("Метод createIsAdmin - Успех")
    void createIsAdmin_whenValidCategoryDto_thenResponseStatusCreatedWithCategoryDtoInBody() throws Exception {
        when(categoryService.create(any(CategoryDto.class)))
                .thenReturn(categoryDto);

        mvc.perform(post(ADMIN_PATH + CATEGORY_PATH)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(categoryDto)));
        verify(categoryService, times(1)).create(any(CategoryDto.class));
    }

    @Test
    @DisplayName("Метод createIsAdmin - Плохие входные данные")
    void createIsAdmin_whenInvalidCategoryDto_thenResponseStatusBadRequest() throws Exception {
        when(categoryService.create(any(CategoryDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(post(ADMIN_PATH + CATEGORY_PATH)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(categoryService, times(1)).create(any(CategoryDto.class));
    }

    @Test
    @DisplayName("Метод createIsAdmin - Не уникальное имя категории")
    void createIsAdmin_whenNotUniqueCategoryName_thenResponseStatusConflict() throws Exception {
        when(categoryService.create(any(CategoryDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(post(ADMIN_PATH + CATEGORY_PATH)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(categoryService, times(1)).create(any(CategoryDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Успех")
    void updateIsAdmin_whenValidCategoryDto_thenResponseStatusOkdWithCategoryDtoInBody() throws Exception {
        when(categoryService.update(anyLong(), any(CategoryDto.class)))
                .thenReturn(categoryDto);

        mvc.perform(patch(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(categoryDto)));
        verify(categoryService, times(1)).update(anyLong(), any(CategoryDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Плохие входные данные")
    void updateIsAdmin_whenInvalidCategoryDto_thenResponseStatusBadRequest() throws Exception {
        when(categoryService.update(anyLong(), any(CategoryDto.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(patch(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(categoryService, times(1)).update(anyLong(), any(CategoryDto.class));
    }

    @Test
    @DisplayName("Метод updateIsAdmin - Не уникальное имя категории")
    void updateIsAdmin_whenNotUniqueCategoryName_thenResponseStatusConflict() throws Exception {
        when(categoryService.update(anyLong(), any(CategoryDto.class)))
                .thenThrow(ConflictException.class);

        mvc.perform(patch(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(categoryService, times(1)).update(anyLong(), any(CategoryDto.class));
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - Успех")
    void deleteIsAdmin_whenValidCategoryIdAndNoRelatedEvents_thenResponseStatusOk() throws Exception {

        mvc.perform(delete(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(categoryService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - Не найдена категория по id")
    void deleteIsAdmin_whenInvalidCategoryId_thenResponseStatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(categoryService).delete(anyLong());

        mvc.perform(delete(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(categoryService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод deleteIsAdmin - У удаляемой категории есть связанные события")
    void deleteIsAdmin_whenThereAreRelatedEvents_thenResponseStatusConflict() throws Exception {
        doThrow(ConflictException.class).when(categoryService).delete(anyLong());

        mvc.perform(delete(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        verify(categoryService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Метод getAllIsPublic - Успех")
    void getAllIsPublic_whenValidAllParams_thenResponseStatusOkWithCategoryDtoCollectionInBody() throws Exception {
        when(categoryService.findAll(any(Pageable.class)))
                .thenReturn(pageCategoryDto);

        mvc.perform(get(CATEGORY_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(pageCategoryDto.getContent())));
        verify(categoryService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getAllIsPublic - Плохие параметры пагинации")
    void getAllIsPublic_whenInvalidPaginationParams_thenResponseStatusBadRequest() throws Exception {
        when(categoryService.findAll(any(Pageable.class)))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(CATEGORY_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(categoryService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Метод getByIdIsPublic - Успех")
    void getByIdIsPublic_whenValidCategoryId_thenResponseStatusOk() throws Exception {
        when(categoryService.findById(anyLong()))
                .thenReturn(categoryDto);

        mvc.perform(get(CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(categoryDto)));
        verify(categoryService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Метод getByIdIsPublic - Не найдена категория по id")
    void getByIdIsPublic_whenInvalidCategoryId_thenResponseStatusNotFound() throws Exception {
        when(categoryService.findById(anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get(CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(categoryService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Метод getByIdIsPublic - Плохой id категории")
    void getByIdIsPublic_whenBadCategoryId_thenResponseStatusBadRequest() throws Exception {
        when(categoryService.findById(anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(get(CATEGORY_PATH + CATEGORY_PREFIX, categoryId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(categoryService, times(1)).findById(anyLong());
    }

}