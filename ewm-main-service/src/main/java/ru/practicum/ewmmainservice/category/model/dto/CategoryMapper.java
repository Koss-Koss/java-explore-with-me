package ru.practicum.ewmmainservice.category.model.dto;

import ru.practicum.ewmmainservice.category.model.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category categoryStorage) {
        return CategoryDto.builder()
                .id(categoryStorage.getId())
                .name(categoryStorage.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
