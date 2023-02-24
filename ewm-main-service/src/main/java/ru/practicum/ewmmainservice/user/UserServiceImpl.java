package ru.practicum.ewmmainservice.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.user.model.User;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;
import ru.practicum.ewmmainservice.user.model.dto.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.EMAIL_ALREADY_EXISTS_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /*@Override
    public UserShortDto findById(long id) {
        return UserMapper.toUserShortDto(userRepository.extract(id));
    }*/

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        try {
            User createdUser = userRepository.save(UserMapper.toUser(userDto));
            log.info("Добавлен пользователь с id = {}", createdUser.getId());
            return UserMapper.toUserDto(createdUser);
        } catch (DataIntegrityViolationException e) {
            log.error(EMAIL_ALREADY_EXISTS_MESSAGE + userDto.getEmail());
            throw new ConflictException(EMAIL_ALREADY_EXISTS_MESSAGE + userDto.getEmail());
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.extract(id);
        userRepository.deleteById(id);
        log.info("Удалён пользователь с id = {}", id);
    }

    @Override
    public Page<UserDto> findAllByIds(Collection<Long> ids, Pageable pageable) {
        Page<User> result;
        if (ids == null || ids.isEmpty()) {
            result = userRepository.findAll(pageable);
        } else {
            result = userRepository.findAllByIdIn(ids, pageable);
        }
        return result.map(UserMapper::toUserDto);
    }
}
