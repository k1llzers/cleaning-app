package com.ukma.cleaning;

import com.ukma.cleaning.user.*;
import com.ukma.cleaning.user.dto.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void setup(@Autowired UserService userService) {
        TestApplication.setupUsers(userService);
    }

    @Test
    @Order(1)
    public void register(){
        var user = new UserRegistrationDto();
        user.setName("John");
        user.setSurname("Doe");
        user.setPatronymic("Patronymic");
        user.setEmail("example@mail.com");
        user.setPassword("P4ss");
        try {
            assert(restTemplate.postForEntity("http://localhost:" + port + "/api/users", user, UserDto.class) == null);
            var _user = restTemplate.getForEntity("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class).getBody();
            assert(_user == null);
        } catch (Exception e) {}
        user.setPassword("P4ssw()rds12");
        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/users", user, UserDto.class);
        
        var userDto = response.getBody();
        assert(response != null);
        assert(userDto.getName().equals(user.getName()));
        assert(userDto.getSurname().equals(user.getSurname()));
        assert(userDto.getEmail().equals(user.getEmail()));
        assert(userDto.getRole().equals(Role.USER));
    }

    @Test
    @Order(2)
    public void update(){
        var user = restTemplate.getForEntity("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class).getBody();
        assert(user != null);
        user.setName("Johnny");
        restTemplate.put("http://localhost:" + port + "/api/users", user);
        user = restTemplate.getForEntity("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class).getBody();
        assert(user != null);
        //FIXME: assert(user.getName().equals("Johnny"));
    }

    @Test
    @Order(3)
    public void changePassword(){
        var user = restTemplate.getForEntity("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class);
        assert(user != null);
        var userDto = user.getBody();
        var userPasswordDto = new UserPasswordDto();
        userPasswordDto.setId(userDto.getId());
        userPasswordDto.setPassword("P4ssw()rds12345");
        restTemplate.put("http://localhost:" + port + "/api/users/pass", userPasswordDto);
    }

    /* 
    @Test
    @Order(4)
    public void delete(){
        var user = restTemplate.getForEntity("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class);
        assert(user != null);
        var userDto = user.getBody();
        restTemplate.delete("http://localhost:" + port + "/api/users/" + userDto.getId());
        try {
            restTemplate.getForEntity("http://localhost:" + port + "/api/users/" + userDto.getId(), UserDto.class);
            assert(false);
        } catch (Exception e) {}
    }
    */

    @Test
    @Order(5)
    @WithMockUser(username = "user2", password = "User2")
    public void userAccess(){
        //TODO: 
    }

    @Test
    @Order(6)
    @WithMockUser(username = "ee@mail.com", password = "12345", roles = "EMPLOYEE")
    public void employeeAccess(){
        //TODO: 
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "Admin", roles = "ADMIN")
    public void adminAccess(){
        //TODO:
    }

    @Test
    @Order(8)
    @WithAnonymousUser
    public void crudAnonymAccess(){
        //TODO:
    }

}
