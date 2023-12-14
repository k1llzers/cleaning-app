package com.ukma.cleaning;

import com.ukma.cleaning.user.*;
import com.ukma.cleaning.user.dto.*;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.Statement;

@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @LocalServerPort
    private int port;

    @Test
    @Order(0)
    public static void setup() {
        try {
            var connection = DriverManager.getConnection("jdbc:h2:mem:cleaning", "sa", "sa");
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET SCHEMA cleaning");
            } catch (Exception e) {
                log.warn("An exception occurred:", e);
            }

            InputStream inputStream = OrderTest.class.getResourceAsStream("resources/testUsers.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            if (inputStream != null) {
                String line;
                Statement statement = connection.createStatement();
                while ((line = reader.readLine()) != null) {
                    if (!line.isEmpty()) {
                        statement.execute(line);
                    }
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
 
    @Test
    @Order(1)
    @WithAnonymousUser
    public void register(){
        var user = new UserRegistrationDto();
        user.setName("John");
        user.setSurname("Doe");
        user.setPatronymic("Patronymic");
        user.setEmail("example@mail.com");
        user.setPassword("P4ss");

        assert(TestTRT.post("http://localhost:" + port + "/api/users", user, UserDto.class).getBody().getEmail() == null);
        assert(TestTRT.get("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class).getBody().getEmail() == null);

        user.setPassword("P4ssw()rds12");
        var response = TestTRT.post("http://localhost:" + port + "/api/users", user, UserDto.class);
        
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
        TestTRT.authAs(2, port);
        var user = TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class).getBody();
        assert(user != null);
        user.setName("J");
        log.warn(user.toString());
        user = TestTRT.put("http://localhost:" + port + "/api/users", user, UserDto.class).getBody();
        user = TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class).getBody();
        assert(user != null);
        assert(user.getName().equals("J"));
    }

    @Test
    @Order(3)
    public void changePassword(){
        var response = TestTRT.get("http://localhost:" + port + "/api/users/pass", UserPasswordDto.class);
        var user = TestTRT.get("http://localhost:" + port + "/api/users/by-email/example@mail.com", UserDto.class);
        assert(user != null);
        var userDto = user.getBody();
        var userPasswordDto = new UserPasswordDto();
        userPasswordDto.setId(userDto.getId());
        userPasswordDto.setPassword("P4ssw()rds12345");
        assert(TestTRT.put("http://localhost:" + port + "/api/users/pass", userPasswordDto, UserDto.class).getBody().getEmail() == null);
    }

    @Test
    @Order(5)
    public void userAccess(){
        //TODO: 
    }

    @Test
    @Order(6)
    public void employeeAccess(){
        //TODO: 
    }

    @Test
    @Order(7)
    public void adminAccess(){
        //TODO:
    }

    @Test
    @Order(8)
    @WithAnonymousUser
    public void AnonymAccess(){
        //TODO:
    }

}
