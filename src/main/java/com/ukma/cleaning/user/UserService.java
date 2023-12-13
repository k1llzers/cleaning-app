package com.ukma.cleaning.user;


import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPageDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto create(UserRegistrationDto user);
    UserDto update(UserDto user);
    UserDto getUser();
    UserDto getByEmail(String email);
    UserDto updatePassword(UserPasswordDto user);

    UserPageDto findUsersByPageAndRole(Role role, Pageable pageable);
}
