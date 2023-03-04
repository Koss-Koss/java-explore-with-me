package ru.practicum.ewmmainservice.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.category.model.Category;
import ru.practicum.ewmmainservice.exception.NotFoundException;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.CATEGORY_NOT_FOUND_MESSAGE;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));
    }
}
