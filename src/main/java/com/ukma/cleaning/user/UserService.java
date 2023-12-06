package com.ukma.cleaning.user;


import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;

public interface UserService {
    UserDto createUser(UserRegistrationDto user);
    UserDto editUser(UserDto user);
    void deleteUser(Long id);
    UserDto getUser(Long id);
    UserDto getUserByEmail(String email);
    UserPasswordDto getUserPassword(Long id);
    UserPasswordDto getUserPasswordByEmail(String email);
    UserDto changePassword(UserPasswordDto user);
}
