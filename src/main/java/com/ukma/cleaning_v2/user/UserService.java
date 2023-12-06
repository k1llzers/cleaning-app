package com.ukma.cleaning_v2.user;


import com.ukma.cleaning_v2.user.dto.UserDto;
import com.ukma.cleaning_v2.user.dto.UserPasswordDto;
import com.ukma.cleaning_v2.user.dto.UserRegistrationDto;

public interface UserService {
    UserDto createUser(UserRegistrationDto user);
    UserDto editUser(UserDto user);
    void deleteUser(long id);
    UserDto getUser(long id);
    UserDto getUserByEmail(String email);
    UserPasswordDto getUserPassword(long id);
    UserPasswordDto getUserPasswordByEmail(String email);
    UserDto changePassword(UserPasswordDto user);
}
