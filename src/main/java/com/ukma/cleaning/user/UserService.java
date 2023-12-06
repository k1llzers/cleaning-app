package com.ukma.cleaning.user;


import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;

public interface UserService {
    UserDto create(UserRegistrationDto user);
    UserDto update(UserDto user);
    Boolean deleteById(Long id);
    UserDto getById(Long id);
    UserDto getByEmail(String email);
    UserDto updatePassword(UserPasswordDto user);
}
