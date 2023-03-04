package ru.practicum.ewmmainservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto create(UserDto userDto);
    Page<UserDto> findAllByIds(Collection<Long> ids, Pageable pageable);
    void delete(long id);
}
