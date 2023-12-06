package com.ukma.cleaning.user;


import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;

public interface UserService {
    UserDto create(UserRegistrationDto user);
    UserDto update(UserDto user);
    void deleteById(Long id);
    UserDto getById(Long id);
    UserDto getByEmail(String email);
    UserPasswordDto getPasswordById(Long id);
    UserPasswordDto getPasswordByEmail(String email);
    UserDto updatePassword(UserPasswordDto user);
}
