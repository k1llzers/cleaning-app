package com.ukma.cleaning;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.ukma.cleaning.order.dto.OrderForUserDto;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:com/ukma/cleaning/resources/test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void setup() {
        try {
            var connection = DriverManager.getConnection("jdbc:h2:mem:cleaning", "sa", "sa");
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET SCHEMA cleaning");
            } catch (Exception e) {
                log.warn("An exception occurred:", e);
            }

            InputStream inputStream = OrderTest.class.getResourceAsStream("resources/testData.sql");
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
    @Order(2)
    public void getOrders() {
        var response = restTemplate.getForEntity("http://localhost:" + port + "/api/orders/user/3", OrderForUserDto.class);
    }
}