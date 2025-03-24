package ru.afishaBMSTU.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.model.user.CustomUserDetails;
import ru.afishaBMSTU.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return userRepository.findByNickname(nickname)
                .map(user -> new CustomUserDetails(user.getId(), user.getNickname(), user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getUserRole().name()))
                                .toList()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}