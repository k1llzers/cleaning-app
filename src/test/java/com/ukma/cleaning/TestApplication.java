package com.ukma.cleaning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserRegistrationDto;

@SpringBootApplication
@PropertySource("classpath:test.properties")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleaningApplication.class, args);
	}

    public static void setupUsers(UserService userService) {
        var user = new UserRegistrationDto();
        user.setName("Johnny");
        user.setSurname("Depp");
        user.setEmail("e@mail.com");
        user.setPassword("12345");
        userService.create(user);

        user = new UserRegistrationDto();
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("ee@mail.com");
        user.setPassword("12345");
        userService.create(user);

        user = new UserRegistrationDto();
        user.setName("Admin");
        user.setSurname("Admin");
        user.setEmail("admin");
        user.setPassword("admin");
        userService.create(user);

        user = new UserRegistrationDto();
        user.setName("User2");
        user.setSurname("User2");
        user.setEmail("User2");
        user.setPassword("User2");
        userService.create(user);
/* 
        var userDto = userService.getById(2l);
        userDto.setRole(Role.EMPLOYEE);
        userService.update(userDto);

        userDto = userService.getById(3l);
        userDto.setRole(Role.ADMIN);
        userService.update(userDto);*/
    }
}

