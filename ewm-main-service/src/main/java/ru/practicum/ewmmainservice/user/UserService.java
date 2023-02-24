package ru.practicum.ewmmainservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    //UserShortDto findById(long id);

    UserDto create(UserDto userDto);

    Page<UserDto> findAllByIds(Collection<Long> ids, Pageable pageable);

    void delete(long id);
}
