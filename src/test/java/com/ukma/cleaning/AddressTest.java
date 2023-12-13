package com.ukma.cleaning;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class AddressTest {

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
    @WithMockUser(username = "e@mail.com", password = "12345", roles = "USER")
    public void createGet() throws Exception {
        /*
         HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<?> request = new HttpEntity<>(headers);

        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/addresses",
            HttpMethod.GET,
            request,
            String.class
        );
        */


        var address = new AddressDto();
        address.setCity("");
        address.setStreet("Khreshchatyk");
        address.setHouseNumber("1");
        address.setFlatNumber("1");

        try {
            var response = restTemplate.postForEntity("http://localhost:" + port + "/api/addresses", address, AddressDto.class);
            assert(response.getStatusCode().is2xxSuccessful() == false);
        } catch (Exception e) {}
        
        address.setCity("Kyiv");
        restTemplate.postForEntity("http://localhost:" + port + "/api/addresses", address, AddressDto.class);

        var address2 = new AddressDto();
        address2.setCity("Kyiv");
        address2.setStreet("Khreshchatyk");
        address2.setHouseNumber("22");
        address2.setFlatNumber("15");
        restTemplate.postForEntity("http://localhost:" + port + "/api/addresses", address2, AddressDto.class);

        var byIdAddress = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/1", AddressDto.class);
        assert(address.getCity().equals(byIdAddress.getCity()));
        assert(address.getStreet().equals(byIdAddress.getStreet()));
        assert(address.getHouseNumber().equals(byIdAddress.getHouseNumber()));
        assert(address.getFlatNumber().equals(byIdAddress.getFlatNumber()));

        var byIdAddress2 = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/2", AddressDto.class);
        assert(address2.getCity().equals(byIdAddress2.getCity()));
        assert(address2.getStreet().equals(byIdAddress2.getStreet()));
        assert(address2.getHouseNumber().equals(byIdAddress2.getHouseNumber()));
        assert(address2.getFlatNumber().equals(byIdAddress2.getFlatNumber()));

        var response = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/by-user/1", List.class);
        List<AddressDto> userAddresses = (List<AddressDto>) response.stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());
            
        assert(userAddresses.size() == 2);
        address.setId(1L);
        assert(userAddresses.contains(address));
        address2.setId(2L);
        assert(userAddresses.contains(address2));
    }
    
    @Test
    @Order(2)
    public void update() {
        var address = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/1", AddressDto.class);
        address.setCity("Lviv");
        address.setStreet("Shevchenka");
        address.setHouseNumber("1");
        address.setFlatNumber("1");
        restTemplate.put("http://localhost:" + port + "/api/addresses", address);

        var newAddress = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/3", AddressDto.class);
        assert(address.getCity().equals(newAddress.getCity()));
        assert(address.getStreet().equals(newAddress.getStreet()));
        assert(address.getHouseNumber().equals(newAddress.getHouseNumber()));
        assert(address.getFlatNumber().equals(newAddress.getFlatNumber()));


        var response = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/by-user/1", List.class);
        List<AddressDto> userAddresses = (List<AddressDto>) response.stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());

        assert(userAddresses.size() == 2);
        assert(userAddresses.get(0).getId() != 1L);
    }

    @Test
    @Order(3)
    public void delete() {
        restTemplate.delete("http://localhost:" + port + "/api/addresses/2");

        var response = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/by-user/1", List.class);
        List<AddressDto> userAddresses = (List<AddressDto>) response.stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());

        assert(userAddresses.size() == 1);
        assert(userAddresses.get(0).getId() == 3L);
    }

    @Test 
    @Order(4)
    @WithMockUser(username = "e@mail.com", password = "12345")
    public void crudThisUserAccess(){
        var address = new AddressDto();
        address.setCity("Kyiv");
        address.setStreet("Khreshchatyk");
        address.setHouseNumber("1");
        address.setFlatNumber("1");
        restTemplate.postForEntity("http://localhost:" + port + "/api/addresses/1", address, AddressDto.class);

        assert(restTemplate.getForEntity("http://localhost:" + port + "/api/addresses/3", AddressDto.class) != null);

        var byIdAddress = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/4", AddressDto.class);
        assert(address.getCity().equals(byIdAddress.getCity()));
        assert(address.getStreet().equals(byIdAddress.getStreet()));
        assert(address.getHouseNumber().equals(byIdAddress.getHouseNumber()));
        assert(address.getFlatNumber().equals(byIdAddress.getFlatNumber()));

        var response = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/by-user/1", List.class);
        List<AddressDto> userAddresses = (List<AddressDto>) response.stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());

        assert(userAddresses.size() == 2);
        address.setId(4L);
        assert(userAddresses.contains(address));

        address.setCity("Lviv");
        address.setStreet("Shevchenka");
        address.setHouseNumber("1");
        address.setFlatNumber("1");
        restTemplate.put("http://localhost:" + port + "/api/addresses", address);
    
        var newAddress = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/5", AddressDto.class);
        assert(address.getCity().equals(newAddress.getCity()));
        assert(address.getStreet().equals(newAddress.getStreet()));
        assert(address.getHouseNumber().equals(newAddress.getHouseNumber()));
        assert(address.getFlatNumber().equals(newAddress.getFlatNumber()));

        restTemplate.delete("http://localhost:" + port + "/api/addresses/3");

        response = restTemplate.getForObject("http://localhost:" + port + "/api/addresses/by-user/1", List.class);
        userAddresses = (List<AddressDto>) response.stream()
            .map(obj -> new ObjectMapper().convertValue(obj, AddressDto.class))
            .collect(Collectors.toList());

        assert(userAddresses.size() == 1);
        assert(userAddresses.get(0).getId() == 5L);
    }

    @Test
    @Order(5)
    @WithMockUser(username = "User2", password = "User2")
    public void crudOtherUserNoAccess()
    {
        crudNoAccess();
    }

    @Test
    @Order(6)
    @WithMockUser(username = "ee@mail.com", password = "12345", roles = "EMPLOYEE")
    public void crudEmployeeNoAccess(){
        crudNoAccess();
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void crudAdminNoAccess(){
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
        try {
            restTemplate.postForEntity("http://localhost:" + port + "/api/addresses/1", address, AddressDto.class);
            assert(false);
        } catch (Exception e) {}
        
        try {
            restTemplate.getForObject("http://localhost:" + port + "/api/addresses/5", AddressDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            restTemplate.getForObject("http://localhost:" + port + "/api/addresses/by-user/1", List.class);
            assert(false);
        } catch (Exception e) {}

        try {
            address.setId(5l);
            restTemplate.put("http://localhost:" + port + "/api/addresses/5", address);
            assert(false);
        } catch (Exception e) {}

        try {
            restTemplate.delete("http://localhost:" + port + "/api/addresses/5");
            assert(false);
        } catch (Exception e) {}
    }
}
