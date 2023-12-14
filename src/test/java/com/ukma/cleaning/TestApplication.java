package com.ukma.cleaning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserRegistrationDto;

@SpringBootApplication
//@PropertySource("classpath:com/ukma/cleaning/resources/application.properties")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleaningApplication.class, args);
	}
}

