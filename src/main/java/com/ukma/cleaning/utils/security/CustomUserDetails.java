package com.ukma.cleaning.utils.security;

import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserEntityByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can`t find user by email: " + username));
    }

    public Boolean register(UserRegistrationDto dto) {
        userService.create(dto);
        return true;
    }
}
