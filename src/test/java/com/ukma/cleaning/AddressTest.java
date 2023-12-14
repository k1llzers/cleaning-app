package com.ukma.cleaning;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.user.UserService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.Statement;

@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:com/ukma/cleaning/resources/test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class AddressTest {

    @LocalServerPort
    private int port;

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
    @Order(2)
    public void createGet() throws Exception {
        TestTRT.authAs(2, port);

        var address = new AddressDto();
        address.setCity("");
        address.setStreet("Khreshchatyk");
        address.setHouseNumber("1");
        address.setFlatNumber("1");
        
        address.setCity("Kyiv");
        var response = TestTRT.post("http://localhost:" + port + "/api/addresses", address, AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());

        var address2 = new AddressDto();
        address2.setCity("Kyiv");
        address2.setStreet("Khreshchatyk");
        address2.setHouseNumber("22");
        address2.setFlatNumber("15");
        response = TestTRT.post("http://localhost:" + port + "/api/addresses", address2, AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());


        response = TestTRT.get("http://localhost:" + port + "/api/addresses/1", AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());
        var byIdAddress = response.getBody();
        assert(address.getCity().equals(byIdAddress.getCity()));
        assert(address.getStreet().equals(byIdAddress.getStreet()));
        assert(address.getHouseNumber().equals(byIdAddress.getHouseNumber()));
        assert(address.getFlatNumber().equals(byIdAddress.getFlatNumber()));

        response = TestTRT.get("http://localhost:" + port + "/api/addresses/2", AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());
        var byIdAddress2 = response.getBody();
        assert(address2.getCity().equals(byIdAddress2.getCity()));
        assert(address2.getStreet().equals(byIdAddress2.getStreet()));
        assert(address2.getHouseNumber().equals(byIdAddress2.getHouseNumber()));
        assert(address2.getFlatNumber().equals(byIdAddress2.getFlatNumber()));

        var responseList = TestTRT.get("http://localhost:" + port + "/api/addresses/by-user", List.class);
        assert(!responseList.getStatusCode().is4xxClientError());
        List<AddressDto> userAddresses = (List<AddressDto>) responseList.getBody().stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());
            
        assert(userAddresses.size() == 2);
        address.setId(1L);
        assert(userAddresses.contains(address));
        address2.setId(2L);
        assert(userAddresses.contains(address2));
    }
    

    @Test
    @Order(3)
    public void update() {
        var response = TestTRT.get("http://localhost:" + port + "/api/addresses/1", AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());
        var address = response.getBody();
        address.setCity("Lviv");
        address.setStreet("Shevchenka");
        address.setHouseNumber("1");
        address.setFlatNumber("1");
        response = TestTRT.put("http://localhost:" + port + "/api/addresses", address, AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());

        response = TestTRT.get("http://localhost:" + port + "/api/addresses/3", AddressDto.class);
        assert(!response.getStatusCode().is4xxClientError());
        var newAddress = response.getBody();
        assert(address.getCity().equals(newAddress.getCity()));
        assert(address.getStreet().equals(newAddress.getStreet()));
        assert(address.getHouseNumber().equals(newAddress.getHouseNumber()));
        assert(address.getFlatNumber().equals(newAddress.getFlatNumber()));


        var responseList = TestTRT.get("http://localhost:" + port + "/api/addresses/by-user", List.class);
        assert(!responseList.getStatusCode().is4xxClientError());
        List<AddressDto> userAddresses = (List<AddressDto>) responseList.getBody().stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());
        assert(userAddresses.size() == 2);
        assert(userAddresses.get(0).getId() != 1L);
    }

    @Test
    @Order(4)
    public void delete() {
        assert(TestTRT.delete("http://localhost:" + port + "/api/addresses/2"));

        var responseList = TestTRT.get("http://localhost:" + port + "/api/addresses/by-user", List.class);
        assert(!responseList.getStatusCode().is4xxClientError());
        List<AddressDto> userAddresses = (List<AddressDto>) responseList.getBody().stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());
        assert(userAddresses.size() == 1);
        assert(userAddresses.get(0).getId() == 3L);
    }

    @Test
    @Order(5)
    public void crudOtherUserNoAccess(){
        TestTRT.authAs(4, port);
        crudNoAccess();
    }

    @Test
    @Order(6)
    public void crudEmployeeNoAccess(){
        TestTRT.authAs(3, port);
        crudNoAccess();
    }

    @Test
    @Order(7)
    public void crudAdminNoAccess(){
        TestTRT.authAs(1, port);
        crudNoAccess();
    }

    @Test
    @Order(8)
    @WithAnonymousUser
    public void crudAnonymNoAccess(){
        crudNoAccess();
    }


    public void crudNoAccess(){
        var address = new AddressDto();
        address.setCity("Kyiv");
        address.setStreet("Khreshchatyk");
        address.setHouseNumber("1");
        address.setFlatNumber("1");

        var response = TestTRT.get("http://localhost:" + port + "/api/addresses/5", AddressDto.class);
        assert(response.getStatusCode().is4xxClientError());
        
        address.setId(5l);
        response = TestTRT.put("http://localhost:" + port + "/api/addresses", address, AddressDto.class);
        assert(response.getStatusCode().is4xxClientError());

        assert(!TestTRT.delete("http://localhost:" + port + "/api/addresses/5", Boolean.class));
    }

}
