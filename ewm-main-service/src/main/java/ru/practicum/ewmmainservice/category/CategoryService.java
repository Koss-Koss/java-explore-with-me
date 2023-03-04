package ru.practicum.ewmmainservice.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(long catId, CategoryDto categoryDto);

    void delete(long id);

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(long id);
}
