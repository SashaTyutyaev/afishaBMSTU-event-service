package ru.afishaBMSTU.service.user.managing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.dto.user.UserUpdateDto;

@Service
@RequiredArgsConstructor
public class MainUserManagingService {

    @Qualifier("emailService")
    private final UserManagingService emailService;

    @Qualifier("phoneService")
    private final UserManagingService phoneService;

    public void addNewItem(Long userId, UserUpdateDto updateDto) {
        String email = updateDto.getEmail();
        String phone = updateDto.getPhone();

        if (email != null) {
            emailService.addNewItem(userId, email);
        }
        if (phone != null) {
            phoneService.addNewItem(userId, phone);
        }
    }

    public void deleteItem(Long itemId, Long userId, String type) {
        switch (type) {
            case "phone":
                phoneService.deleteItem(itemId, userId);
                return;
            case "email":
                emailService.deleteItem(itemId, userId);
        }
    }

    public void updateItem(Long itemId, Long userId, UserUpdateDto updateDto) {
        String email = updateDto.getEmail();
        String phone = updateDto.getPhone();

        if (email != null) {
            emailService.updateItem(itemId, userId, email);
        }
        if (phone != null) {
            phoneService.updateItem(itemId, userId, phone);
        }
    }
}