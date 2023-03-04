package ru.practicum.ewmmainservice.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.category.model.Category;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;
import ru.practicum.ewmmainservice.category.model.dto.CategoryMapper;
import ru.practicum.ewmmainservice.event.EventService;
import ru.practicum.ewmmainservice.exception.ConflictException;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.CATEGORY_ALREADY_EXISTS_MESSAGE;
import static ru.practicum.ewmmainservice.exception.errormessage
        .ErrorMessageConstants.DELETE_CATEGORY_WITH_RELATED_EVENTS_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventService eventService;

    @Transactional
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        try {
            Category createdCategory = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
            log.info("Добавлена категория с id = {}", createdCategory.getId());
            return CategoryMapper.toCategoryDto(createdCategory);
        } catch (DataIntegrityViolationException e) {
            log.error(CATEGORY_ALREADY_EXISTS_MESSAGE + categoryDto.getName());
            throw new ConflictException(CATEGORY_ALREADY_EXISTS_MESSAGE + categoryDto.getName());
        }
    }

    @Transactional
    @Override
    public CategoryDto update(long id, CategoryDto categoryDto) {
        Category currentCategory = categoryRepository.extract(id);
        currentCategory.setName(categoryDto.getName());
        try {
            Category updatedCategory = categoryRepository.save(currentCategory);
            log.info("Обновлена категория с id = {}", id);
            return CategoryMapper.toCategoryDto(updatedCategory);
        } catch (DataIntegrityViolationException e) {
            log.error(CATEGORY_ALREADY_EXISTS_MESSAGE + categoryDto.getName());
            throw new ConflictException(CATEGORY_ALREADY_EXISTS_MESSAGE + categoryDto.getName());
        }
    };

    @Transactional
    @Override
    public void delete(long id) {
        categoryRepository.extract(id);
        if (eventService.existsCategoryRelatedEvents(id)) {
            throw new ConflictException(DELETE_CATEGORY_WITH_RELATED_EVENTS_MESSAGE);
        }
        categoryRepository.deleteById(id);
        log.info("Удалена категория с id = {}", id);
    }

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryMapper::toCategoryDto);
    }

    @Override
    public CategoryDto findById(long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.extract(id));
    }
}
