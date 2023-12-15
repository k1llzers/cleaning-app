package com.ukma.cleaning;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.employment.EmploymentDto;
import com.ukma.cleaning.employment.EmploymentRepository;
import com.ukma.cleaning.user.Role;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.utils.security.JwtService;
import com.ukma.cleaning.utils.security.SecurityContextAccessor;
import com.ukma.cleaning.utils.security.dto.AuthRequest;
import com.ukma.cleaning.utils.security.dto.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class EmploymentTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @LocalServerPort
    private int port;
    private HttpHeaders headers;
    private static RestTemplate restTemplate;

    @BeforeAll
    static void prepareAdmin(@Autowired PasswordEncoder passwordEncoder) {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void prepareUsers() {
        UserEntity admin = new UserEntity();
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        UserEntity user = new UserEntity();
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("admin@gmail.com");
        authRequest.setPassword("password");
        ResponseEntity<JwtResponse> s = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", authRequest, JwtResponse.class);
        JwtResponse response = s.getBody();
        headers = new HttpHeaders();
        headers.add("Cookie", "accessToken=" + response.getAccessToken() + "; refreshToken=" + response.getRefreshToken());
    }

    @Test
    void createRequestTest() {
        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        ResponseEntity<EmploymentDto> requestResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);
        assertTrue(requestResponse.getStatusCode().is2xxSuccessful());
        EmploymentDto createdRequest = requestResponse.getBody();
        assertAll(
                () -> assertNotNull(createdRequest),
                () -> assertNotEquals(createdRequest.getId(), 0),
                () -> assertNotNull(createdRequest.getApplicant()),
                () -> assertEquals(createdRequest.getApplicant().getEmail(), "user@gmail.com"),
                () -> assertEquals(createdRequest.getApplicant().getRole(), Role.USER)
        );
    }

    @Test
    void acceptRequestTest() {
        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);

        HttpEntity<String> requestEntity1 = new HttpEntity<>(this.headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:" + port + "/api/employment/2/succeed", HttpMethod.PUT, requestEntity1, Boolean.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody());
        UserEntity user = userRepository.findById(2L).orElseThrow(RuntimeException::new);
        assertEquals(user.getRole(), Role.EMPLOYEE);
    }

    @Test
    void createRequest_EmployedUser_ShouldThrow() {
        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);

        HttpEntity<String> requestEntity1 = new HttpEntity<>(this.headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:" + port + "/api/employment/2/succeed", HttpMethod.PUT, requestEntity1, Boolean.class);

        assertThrows(Exception.class, () ->restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class));
    }

    @Test
    void declineRequestTest() {
        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);

        HttpEntity<String> requestEntity1 = new HttpEntity<>(this.headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:" + port + "/api/employment/2/cancel", HttpMethod.PUT, requestEntity1, Boolean.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody());
        UserEntity user = userRepository.findById(2L).orElseThrow(RuntimeException::new);
        assertEquals(user.getRole(), Role.USER);
    }

    @Test
    void fireEmployeeTest() {
        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);

        HttpEntity<String> requestEntity1 = new HttpEntity<>(this.headers);
        restTemplate.exchange("http://localhost:" + port + "/api/employment/2/succeed", HttpMethod.PUT, requestEntity1, Boolean.class);

        HttpEntity<String> requestEntity2 = new HttpEntity<>(this.headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:" + port + "/api/employment/2/unemployment", HttpMethod.PUT, requestEntity2, Boolean.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody());
        UserEntity user = userRepository.findById(2L).orElseThrow(RuntimeException::new);
        assertEquals(user.getRole(), Role.USER);
    }

    @Test
    void getAllEmploymentRequestsTest() {
        addUserToDB("user1@gmail.com", "password");
        addUserToDB("user2@gmail.com", "password");

        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);
        HttpHeaders headers1 = getHeaderWithToken("user1@gmail.com", "password");
        HttpEntity<String> requestEntity1 = new HttpEntity<>("Motivation list 1", headers1);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity1, EmploymentDto.class);
        HttpHeaders headers2 = getHeaderWithToken("user2@gmail.com", "password");
        HttpEntity<String> requestEntity2 = new HttpEntity<>("Motivation list 2", headers2);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity2, EmploymentDto.class);

        HttpEntity<String> requestEntityAdm = new HttpEntity<>(this.headers);
        ResponseEntity<List> response = restTemplate.exchange("http://localhost:" + port + "/api/employment", HttpMethod.GET, requestEntityAdm, List.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

    }

    @Test
    void acceptOneRequestAndGetAll(@Autowired EmploymentRepository employmentRepository) {
        addUserToDB("user1@gmail.com", "password");
        addUserToDB("user2@gmail.com", "password");

        HttpHeaders headers = getHeaderWithToken("user@gmail.com", "password");
        HttpEntity<String> requestEntity = new HttpEntity<>("Motivation list", headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity, EmploymentDto.class);

        HttpHeaders headers1 = getHeaderWithToken("user1@gmail.com", "password");
        HttpEntity<String> requestEntity1 = new HttpEntity<>("Motivation list 1", headers1);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity1, EmploymentDto.class);

        HttpHeaders headers2 = getHeaderWithToken("user2@gmail.com", "password");
        HttpEntity<String> requestEntity2 = new HttpEntity<>("Motivation list 2", headers2);
        restTemplate.postForEntity("http://localhost:" + port + "/api/employment", requestEntity2, EmploymentDto.class);

        HttpEntity<String> requestEntityAdm = new HttpEntity<>(this.headers);
        ResponseEntity<List> response = restTemplate.exchange("http://localhost:" + port + "/api/employment", HttpMethod.GET, requestEntityAdm, List.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        ResponseEntity<Boolean> responseAdmin = restTemplate.exchange("http://localhost:" + port + "/api/employment/2/succeed", HttpMethod.PUT, requestEntityAdm, Boolean.class);
        assertTrue(responseAdmin.getStatusCode().is2xxSuccessful());
        assertTrue(responseAdmin.getBody());


        ResponseEntity<List> response2 = restTemplate.exchange("http://localhost:" + port + "/api/employment", HttpMethod.GET, requestEntityAdm, List.class);
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertNotNull(response2.getBody());
        assertEquals(2, response2.getBody().size());
        assertAll(
                () -> assertTrue(userRepository.findById(2L).get().getRole().equals(Role.EMPLOYEE)),
                () -> assertTrue(userRepository.findById(3L).get().getRole().equals(Role.USER)),
                () -> assertTrue(userRepository.findById(4L).get().getRole().equals(Role.USER))
        );
    }

    private void addUserToDB(String name, String pass) {
        UserEntity user = new UserEntity();
        user.setEmail(name);
        user.setPassword(passwordEncoder.encode(pass));
        user.setRole(Role.USER);
        userRepository.save(user);
    }


    private JwtResponse getTokenByUser(String name, String pass) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(name);
        authRequest.setPassword(pass);
        ResponseEntity<JwtResponse> s = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", authRequest, JwtResponse.class);
        return s.getBody();
    }

    //TODO adm = 1, user = 2
    private HttpHeaders getHeaderWithToken(String name, String pass) {
        JwtResponse response = getTokenByUser(name, pass);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "accessToken=" + response.getAccessToken() + "; refreshToken=" + response.getRefreshToken());
        return headers;
    }
}
