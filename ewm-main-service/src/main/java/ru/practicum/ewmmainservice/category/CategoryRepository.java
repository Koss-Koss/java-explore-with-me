package ru.practicum.ewmmainservice.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.category.model.Category;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.user.model.User;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.CATEGORY_NOT_FOUND_MESSAGE;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.USER_NOT_FOUND_MESSAGE;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));
    }
}
