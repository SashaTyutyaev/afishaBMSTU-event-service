package ru.afishaBMSTU.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.user.UserDto;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.UserMapper;
import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        try {
            User user = UserMapper.toUser(userDto);
            userRepository.save(user);
            UserDto savedUser = UserMapper.toUserDto(user);
            log.info("Add user: {}", savedUser);
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            log.error("User with email: {} already exists", userDto.getEmail());
            throw new DataIntegrityViolationException("User with email: " + userDto.getEmail() + " already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        Page<User> users;
        Pageable pageable = validatePageable(from, size);
        if (ids != null) {
            if (ids.isEmpty()) {
                log.info("ids is empty, returning empty list");
                return Collections.emptyList();
            }
            log.info("ids is not null, returning list with page from {} to {}", from, size);
            users = userRepository.findAllByIdsPageable(ids, pageable);
        } else {
            log.info("ids is null, returning all users");
            users = userRepository.findAll(pageable);
        }
        return users.getContent().stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
    }

    private PageRequest validatePageable(Integer from, Integer size) {
        if (from == null || from < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }
        if (size == null || size < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }

        return PageRequest.of(from / size, size);
    }

    private void getUserById(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found", id);
            return new NotFoundException("The user with id " + id + " not found");
        });
    }
}
