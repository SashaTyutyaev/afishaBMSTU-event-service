package ru.afishaBMSTU.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.user.UserFullDto;
import ru.afishaBMSTU.dto.user.UserRegisterRequest;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.UserMapper;
import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.repository.UserRepository;
import ru.afishaBMSTU.security.SecurityContextService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextService securityContextService;

    @Override
    @Transactional
    public UserFullDto registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByNickname(userRegisterRequest.getNickname())) {
            throw new IncorrectParameterException("User nickname already exists");
        }

        User user = userMapper.toUser(userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        User savedUser = userRepository.save(user);

        UserFullDto response = userMapper.toUserFullDto(user);
        log.info("Add user: {}", savedUser);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullDto getUser(Long id) {
        return userMapper.toUserFullDto(getUserById(id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        securityContextService.clearContextForUser(id);
        log.info("User deleted: {}", id);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found", id);
            return new NotFoundException("The user with id " + id + " not found");
        });
    }
}
