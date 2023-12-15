package com.ukma.cleaning;

import com.ukma.cleaning.user.dto.*;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @LocalServerPort
    private int port;

    @Test
    @Order(1)
    public void setup() {
        TestApplication.setupUsers();
    }
 
    @Test
    @Order(2)
    @WithAnonymousUser
    public void get(){
        TestTRT.clear();
        TestTRT.authAs(2, port);
        
        var response = TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class);
        assert(response != null);
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        assert(response.getBody().getEmail().equals("m.burnatt@gmail.com"));
    }

    @Test
    @Order(3)
    @WithAnonymousUser
    public void update(){
        TestTRT.authAs(2, port);
        var user = TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class).getBody();
        assert(user != null);
        user.setName("J");
        user = TestTRT.put("http://localhost:" + port + "/api/users", user, UserDto.class).getBody();
        user = TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class).getBody();
        assert(user != null);
        assert(user.getName().equals("J"));
    }

    @Test
    @Order(4)
    public void changePassword(){
        var user = TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class);
        assert(user != null);
        var userDto = user.getBody();
        var userPasswordDto = new UserPasswordDto();
        userPasswordDto.setId(userDto.getId());
        userPasswordDto.setPassword("P4ssw()rds12345");
        var newDto = TestTRT.put("http://localhost:" + port + "/api/users/pass", userPasswordDto, UserDto.class).getBody();
        assert(newDto != null);
        assert(newDto.getEmail().equals(userDto.getEmail()));
    }

    @Test
    @Order(5)
    public void userAccesses(){
        TestTRT.authAs(2, port);
        try {
            TestTRT.get("http://localhost:" + port + "/api/users/by-email/m.burnatt@gmail.com", UserDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/users/findAllByRole", UserPageDto.class);
            assert(false);
        } catch (Exception e) {}
    }

    @Test
    @Order(6)
    public void employeeAccesses(){
        TestTRT.authAs(3, port);
        try {
            TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/users/by-email/m.burnatt@gmail.com", UserDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/users/findAllByRole", UserPageDto.class);
            assert(false);
        } catch (Exception e) {}
    }

    @Test
    @Order(7)
    public void adminAccesses(){
        TestTRT.authAs(1, port);
        try {
            TestTRT.get("http://localhost:" + port + "/api/users/by-email/m.burnatt@gmail.com", UserDto.class);
        } catch (Exception e) {assert(false);}

        try {
            TestTRT.get("http://localhost:" + port + "/api/users/findAllByRole", UserPageDto.class);
        } catch (Exception e) {assert(false);}
    }

    @Test
    @Order(8)
    @WithAnonymousUser
    public void anonymAccesses(){
        TestTRT.clear();
        try {
            TestTRT.get("http://localhost:" + port + "/api/users", UserDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/users/by-email/m.burnatt@gmail.com", UserDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/users/findAllByRole", UserPageDto.class);
            assert(false);
        } catch (Exception e) {}
    }

}
