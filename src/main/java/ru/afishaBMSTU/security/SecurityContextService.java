package ru.afishaBMSTU.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.afishaBMSTU.model.user.CustomUserDetails;

@Service
public class SecurityContextService {

    public void clearContextForUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails.getId().equals(userId)) {
                SecurityContextHolder.clearContext();
            }
        }
    }
}
