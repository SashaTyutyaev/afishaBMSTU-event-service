package ru.afishaBMSTU.service.user.managing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.model.email.Email;
import ru.afishaBMSTU.model.user.User;
import ru.afishaBMSTU.repository.EmailRepository;
import ru.afishaBMSTU.repository.UserRepository;

@Service("emailService")
@RequiredArgsConstructor
@Slf4j
public class EmailService implements UserManagingService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addNewItem(Long userId, String item) {
        isEmailAvailable(item);

        User user = getUserById(userId);
        Email email = Email.builder()
                .user(user)
                .email(item)
                .build();

        user.getEmails().add(email);
        userRepository.save(user);

        log.info("Successfully added new Email to user {}", userId);
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        User user = getUserById(userId);
        Email email = getEmailById(itemId);

        user.getEmails().remove(email);
        userRepository.save(user);

        log.info("Successfully deleted Email of user {}", userId);
    }

    @Override
    @Transactional
    public void updateItem(Long itemId, Long userId, String item) {
        isEmailAvailable(item);

        User user = getUserById(userId);
        Email existedEmail = getEmailById(itemId);

        existedEmail.setEmail(item);
        user.getEmails().add(existedEmail);
        userRepository.save(user);

        log.info("Successfully updated Email for user {}", userId);
    }

    private void isEmailAvailable(String email) {
        if (emailRepository.findUserIdByEmail(email).isPresent()) {
            throw new IncorrectParameterException("Email is already using");
        }
    }

    private Email getEmailById(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Email not found"));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
