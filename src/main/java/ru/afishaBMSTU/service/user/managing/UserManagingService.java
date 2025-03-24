package ru.afishaBMSTU.service.user.managing;

public interface UserManagingService {
    void addNewItem(Long userId, String item);
    void deleteItem(Long itemId, Long userId);
    void updateItem(Long itemId, Long userId, String item);
}