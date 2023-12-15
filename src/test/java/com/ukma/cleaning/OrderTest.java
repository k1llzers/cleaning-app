package com.ukma.cleaning;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.order.dto.OrderCreationDto;
import com.ukma.cleaning.order.dto.OrderForAdminDto;
import com.ukma.cleaning.order.dto.OrderForUserDto;
import com.ukma.cleaning.order.dto.OrderListDto;
import com.ukma.cleaning.order.dto.OrderPageDto;
import com.ukma.cleaning.user.dto.EmployeeDto;

import lombok.extern.slf4j.Slf4j;

import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    @LocalServerPort
    private int port;

    @AfterEach
    public void clear(){
        try {
            var connection = DriverManager.getConnection("jdbc:h2:mem:cleaning", "sa", "sa");
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET SCHEMA cleaning");
            } catch (Exception e) {
                log.warn("An exception occurred:", e);
            }
            var statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM refresh_tokens");
            statement.close();
            connection.close();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void setup() {
        TestApplication.setupAll();
    }

    @Test
    @Order(2)
    public void get() {
        TestTRT.authAs(2, port);
        var response = TestTRT.get("http://localhost:" + port + "/api/orders/user/6", OrderForUserDto.class);
        assert(response != null);
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        assert(response.getBody().getPrice() == 1000.0);

        var responseList = TestTRT.get("http://localhost:" + port + "/api/orders/by-user/2", OrderPageDto.class);
        assert(responseList != null);
        assert(responseList.getStatusCode().is2xxSuccessful());
        assert(responseList.getBody() != null);
        assert(responseList.getBody().getOrderList().size() == 3);
    }

    @Test
    @Order(3)
    public void getAdmin() {
        TestTRT.authAs(1, port);
        var response = TestTRT.get("http://localhost:" + port + "/api/orders/admin/5", OrderForAdminDto.class);
        assert(response != null);
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        assert(response.getBody().getPrice() == 1100.0);

        var responsePage = TestTRT.get("http://localhost:" + port + "/api/orders/all/by-status", OrderPageDto.class);
        assert(responsePage != null);
        assert(responsePage.getStatusCode().is2xxSuccessful());
        assert(responsePage.getBody() != null);
        assert(responsePage.getBody().getOrderList().size() == 1);

        var responseAll = TestTRT.get("http://localhost:" + port + "/api/orders/all", OrderPageDto.class);
        assert(responseAll != null);
        assert(responseAll.getStatusCode().is2xxSuccessful());
        assert(responseAll.getBody() != null);
        assert(responseAll.getBody().getOrderList().size() == 4);
    }

    @Test
    @Order(4)
    public void create() {
        TestTRT.authAs(2, port);
        var order = new OrderCreationDto();
        order.setAddress(TestTRT.get("http://localhost:" + port + "/api/addresses/1", AddressDto.class).getBody());
        order.setPrice(1000.0);
        order.setOrderTime(LocalDateTime.now());
        order.setClientId(2L);
        order.setDuration(java.time.Duration.ofHours(5));
        var proposals = new HashMap<Long, Integer>();
        proposals.put(1L, 1);
        proposals.put(2L, 1);
        proposals.put(2L, 5);

        var response = TestTRT.post("http://localhost:" + port + "/api/orders", order, OrderCreationDto.class);
        assert(response.getStatusCode().is4xxClientError());

        order.setProposals(proposals);

        response = TestTRT.post("http://localhost:" + port + "/api/orders", order, OrderCreationDto.class);
        assert(response != null);
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        assert(response.getBody().getPrice() == 1000.0);

        order.setPrice(1005.0);

        response = TestTRT.post("http://localhost:" + port + "/api/orders", order, OrderCreationDto.class);
        assert(response != null);
        assert(response.getStatusCode().is2xxSuccessful());
        assert(response.getBody() != null);
        assert(response.getBody().getPrice() == 1005.0);

        var responseList = TestTRT.get("http://localhost:" + port + "/api/orders/by-user/2", OrderPageDto.class);
        assert(responseList != null);
        assert(responseList.getStatusCode().is2xxSuccessful());
        assert(responseList.getBody() != null);
        assert(responseList.getBody().getOrderList().size() == 5);
    }

    @Test
    @Order(5)
    public void updateUser(){
        var order = TestTRT.get("http://localhost:" + port + "/api/orders/user/2", OrderForUserDto.class).getBody();
        order.setPrice(1010.0);
        order.setStatus(Status.CANCELLED);
        order = TestTRT.put("http://localhost:" + port + "/api/orders/user", order, OrderForUserDto.class).getBody();
        assert(order.getPrice() == 1005.0);
        assert(order.getStatus() == Status.CANCELLED);
    }

    @Test
    @Order(6)
    public void updateAdmin(){
        TestTRT.authAs(1, port);
        var order = TestTRT.get("http://localhost:" + port + "/api/orders/admin/1", OrderForAdminDto.class).getBody();
        var employeeList = TestTRT.get("http://localhost:" + port + "/api/available/employees/1", List.class).getBody();
        List<EmployeeDto> employees = (List<EmployeeDto>) employeeList.stream()
            .map(obj -> new ObjectMapper().convertValue(obj, EmployeeDto.class))
            .collect(Collectors.toList());
        order.setExecutors(List.of(employees.get(0)));
        order.setStatus(Status.VERIFIED);
        var updatedOrder = TestTRT.put("http://localhost:" + port + "/api/orders/admin", order, OrderForAdminDto.class).getBody();
        assert(updatedOrder.getExecutors().size() == 1);
        assert(updatedOrder.getExecutors().get(0).getId() == 3L);
        assert(updatedOrder.getStatus() == Status.VERIFIED);
    }

    @Test
    @Order(7)
    public void updateEmployee(){
        TestTRT.authAs(3, port);
        var orderFromList = TestTRT.putNothing("http://localhost:" + port + "/api/orders/change/status/1/PREPARING", OrderListDto.class).getBody();
        assert(orderFromList.getStatus() == Status.PREPARING);
    }

    @Test
    @Order(8)
    public void delete(){
        TestTRT.authAs(2, port);
        var response1 = TestTRT.get("http://localhost:" + port + "/api/orders/user/8", OrderForUserDto.class).getBody();
        var result = TestTRT.delete("http://localhost:" + port + "/api/orders/8", Boolean.class);
        var response2 = TestTRT.get("http://localhost:" + port + "/api/orders/user/8", OrderForUserDto.class).getBody();
        assert(result);
        assert(response1.getStatus() == Status.NOT_VERIFIED);
        assert(response2.getStatus() == Status.CANCELLED);
        assert(response2.getPrice().equals(response1.getPrice()));
        assert(response2.getDuration().equals(response1.getDuration()));
    }

    @Test
    @Order(9)
    public void userAccesses(){
        TestTRT.clear();
        TestTRT.authAs(5, port);
        adminExclusiveAccess();
        employeeExclusiveAccess();
        user2ExclusiveAccess();
    }

    @Test
    @Order(10)
    public void employeeAccesses(){
        TestTRT.authAs(3, port);
        adminExclusiveAccess();
        user2ExclusiveAccess();
    }

    @Test
    @Order(11)
    public void adminAccesses(){
        TestTRT.authAs(1, port);
        employeeExclusiveAccess();
        user2ExclusiveAccess();
    }

    @Test
    @Order(12)
    @WithAnonymousUser
    public void anonymAccesses(){
        TestTRT.clear();
        adminExclusiveAccess();
        employeeExclusiveAccess();
        user2ExclusiveAccess();
    }

    public void adminExclusiveAccess(){
        try {
            TestTRT.get("http://localhost:" + port + "/api/orders/admin/5", OrderForAdminDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/orders/all/by-status", OrderPageDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            TestTRT.get("http://localhost:" + port + "/api/orders/all", OrderPageDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            OrderForAdminDto order = new OrderForAdminDto();
            TestTRT.put("http://localhost:" + port + "/api/orders/admin", order, OrderPageDto.class);
            assert(false);
        } catch (Exception e) {}

    }

    public void employeeExclusiveAccess(){
        try {
            TestTRT.get("http://localhost:" + port + "/api/orders/employee/1", OrderForUserDto.class);
            assert(false);
        } catch (Exception e) {}
    }

    public void user2ExclusiveAccess(){
        try {
            var response = TestTRT.get("http://localhost:" + port + "/api/orders/user/5", OrderForUserDto.class);
            assert(response.getStatusCode().is4xxClientError());
        } catch (Exception e) {}

        assert(!TestTRT.delete("http://localhost:" + port + "/api/orders/user/6", Boolean.class));
    }
}