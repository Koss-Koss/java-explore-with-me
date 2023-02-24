package ru.practicum.ewmmainservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.user.model.User;

import java.util.Collection;
import java.util.List;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.USER_NOT_FOUND_MESSAGE;

public interface UserRepository extends JpaRepository<User, Long> {

    default User extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND_MESSAGE + id));
    }

    Page<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
