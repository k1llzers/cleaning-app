package com.ukma.cleaning;

import com.ukma.cleaning.address.AddressDto;
import com.ukma.cleaning.address.AddressEntity;
import com.ukma.cleaning.address.AddressRepository;
import com.ukma.cleaning.commercialProposal.ComercialProposalType;
import com.ukma.cleaning.commercialProposal.CommercialProposalEntity;
import com.ukma.cleaning.commercialProposal.CommercialProposalRepository;
import com.ukma.cleaning.order.OrderEntity;
import com.ukma.cleaning.order.OrderRepository;
import com.ukma.cleaning.order.Status;
import com.ukma.cleaning.user.Role;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.user.dto.EmployeeDto;
import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.utils.security.dto.AuthRequest;
import com.ukma.cleaning.utils.security.dto.JwtResponse;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenEntity;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class BookingTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CommercialProposalRepository commercialProposalRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @LocalServerPort
    private int port;
    private static RestTemplate restTemplate;

    @BeforeEach
    void prepare() {
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Kyiv");
        addressDto.setStreet("BorysoHlibska");
        addressDto.setHouseNumber("5a");
        HttpHeaders headers = getHeaderWithToken("user1@gmail.com","password");
        HttpEntity<AddressDto> requestEntity = new HttpEntity<>(addressDto,headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/addresses", requestEntity, AddressDto.class);
    }
    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAvailableTimeTest_NoOrders_AllAvailable() {
        HttpEntity<String> requestEntityAdm = new HttpEntity<>(getHeaderWithToken("user@gmail.com", "password"));
        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:" + port + "/api/available/time/2/PT15M", HttpMethod.GET, requestEntityAdm, Map.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        Map<LocalDate, List<LocalTime>> availableTime = (Map<LocalDate, List<LocalTime>>) response.getBody();
        System.out.println();
        assertEquals(7, availableTime.size());
        for (int i = 0; i < availableTime.size(); i++) {
            assertEquals(12, availableTime.get(LocalDate.now().plusDays(i + 1).toString()).size());
        }
    }

    @Test
    void getAvailableEmployeesTest_UserTryToGet_ShouldThrow() {
        HttpEntity<String> requestEntityAdm = new HttpEntity<>(getHeaderWithToken("user@gmail.com", "password"));
        CommercialProposalEntity proposalEntity = commercialProposalRepository.findById(1L).get();
        OrderEntity orderEntity = getOrder(2, proposalEntity, 1, LocalDate.now().plusDays(1).atTime(15,0));
        orderRepository.save(orderEntity);

        assertThrows(Exception.class,
                () -> restTemplate.exchange("http://localhost:" + port + "/api/available/employees/1", HttpMethod.GET, requestEntityAdm, List.class)
        );
    }

    @Test
    void getAvailableEmployeesTest_OneOrder_AllAvailable() {
        HttpEntity<String> requestEntityAdm = new HttpEntity<>(getHeaderWithToken("admin@gmail.com", "password"));
        CommercialProposalEntity proposalEntity = commercialProposalRepository.findById(1L).get();
        OrderEntity orderEntity = getOrder(2, proposalEntity, 1, LocalDate.now().plusDays(1).atTime(15,0));
        long id = orderRepository.save(orderEntity).getId();

        ResponseEntity<List> response = restTemplate.exchange("http://localhost:" + port + "/api/available/employees/" + id, HttpMethod.GET, requestEntityAdm, List.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        List<EmployeeDto> availableEmployees = (List<EmployeeDto>) response.getBody();
        assertEquals(7, availableEmployees.size());
        System.out.println();
    }

    @Test
    @SuppressWarnings("all")
    void getAvailableTimeTest_OneOrderNotVerified() {
        HttpEntity<String> requestEntityAdm = new HttpEntity<>(getHeaderWithToken("user@gmail.com", "password"));
        CommercialProposalEntity proposalEntity = commercialProposalRepository.findById(1L).get();
        OrderEntity orderEntity = getOrder(2, proposalEntity, 1, LocalDate.now().plusDays(1).atTime(15,0));
        orderRepository.save(orderEntity);


        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:" + port + "/api/available/time/6/PT1H", HttpMethod.GET, requestEntityAdm, Map.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        Map<LocalDate, List<LocalTime>> availableTime = (Map<LocalDate, List<LocalTime>>) response.getBody();
        assertEquals(7, availableTime.size());
        assertEquals(8, availableTime.get(LocalDate.now().plusDays(1).toString()).size());
        assertTrue(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("13:00:00"));

        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("14:00:00"));
        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("15:00:00"));
        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("16:00:00"));
        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("17:00:00"));

        assertTrue(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("18:00:00"));
    }

    @Test
    @SuppressWarnings("all")
    void getAvailableTimeTest_OneOrderNotVerified_OneVerified() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHeaderWithToken("user@gmail.com", "password"));
        CommercialProposalEntity proposalEntity = commercialProposalRepository.findById(3L).get();
        OrderEntity orderEntity = getOrder(2, proposalEntity, 1, LocalDate.now().plusDays(1).atTime(15,0));
        orderEntity.setStatus(Status.VERIFIED);

        orderRepository.save(orderEntity);
        OrderEntity orderEntity2 = getOrder(2, proposalEntity, 1,LocalDate.now().plusDays(1).atTime(18,0));
        orderRepository.save(orderEntity2);


        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:" + port + "/api/available/time/6/PT1H", HttpMethod.GET, requestEntity, Map.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        Map<LocalDate, List<LocalTime>> availableTime = (Map<LocalDate, List<LocalTime>>) response.getBody();
        System.out.println();
        assertEquals(7, availableTime.size());
        assertEquals(5, availableTime.get(LocalDate.now().plusDays(1).toString()).size());
        assertTrue(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("13:00:00"));

        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("14:00:00"));
        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("17:00:00"));
        assertFalse(availableTime.get(LocalDate.now().plusDays(1).toString()).contains("20:00:00"));
    }

    @Test
    void getAvailableTimeTest_allEmployeesAreBusy() {
        HttpEntity<String> requestEntity = new HttpEntity<>(getHeaderWithToken("user@gmail.com", "password"));
        CommercialProposalEntity proposalEntity = commercialProposalRepository.findById(4L).get();
        orderRepository.save(getOrder(2, proposalEntity, 4, LocalDate.now().plusDays(1).atTime(10,0)));
        orderRepository.save(getOrder(2, proposalEntity, 4, LocalDate.now().plusDays(1).atTime(14,0)));
        orderRepository.save(getOrder(2, proposalEntity, 4, LocalDate.now().plusDays(1).atTime(16,0)));
        orderRepository.save(getOrder(2, proposalEntity, 4, LocalDate.now().plusDays(1).atTime(20,0)));

        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:" + port + "/api/available/time/6/PT1H", HttpMethod.GET, requestEntity, Map.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        Map<LocalDate, List<LocalTime>> availableTime = (Map<LocalDate, List<LocalTime>>) response.getBody();
        assertEquals(0, availableTime.get(LocalDate.now().plusDays(1).toString()).size());
    }


    private OrderEntity getOrder(int durationInHours, CommercialProposalEntity commercialProposal, int numOfProposals, LocalDateTime orderTime) {
        UserEntity userEntity = userRepository.findById(1L).get();
        AddressEntity addressEntity = addressRepository.findById(1L).get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setClient(userEntity);
        orderEntity.setAddress(addressEntity);
//        orderEntity.setOrderTime(LocalDate.now().plusDays(1).atTime(15,0));
        orderEntity.setOrderTime(orderTime);
        orderEntity.setCreationTime(LocalDateTime.now());
        orderEntity.setDuration(Duration.ofHours(durationInHours));
        orderEntity.setStatus(Status.NOT_VERIFIED);
        orderEntity.setPrice(100.0);
        Map<CommercialProposalEntity, Integer> map = new HashMap<>();
        map.put(commercialProposal, numOfProposals);
        orderEntity.setCommercialProposals(map);
        return orderEntity;
    }

    private static void addUserToDB(@Autowired PasswordEncoder passwordEncoder, @Autowired UserRepository userRepository,
                                    String name, String pass,  Role role) {
        UserEntity user = new UserEntity();
        user.setEmail(name);
        user.setPassword(passwordEncoder.encode(pass));
        user.setRole(role);
        userRepository.save(user);
    }
    private JwtResponse getTokenByUser(String name, String pass) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(name);
        authRequest.setPassword(pass);
        ResponseEntity<JwtResponse> s = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", authRequest, JwtResponse.class);
        return s.getBody();
    }
    private HttpHeaders getHeaderWithToken(String name, String pass) {
        JwtResponse response = getTokenByUser(name, pass);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie","accessToken=" + response.getAccessToken() + "; refreshToken="+ response.getRefreshToken());
        return headers;
    }

    @BeforeAll
    static void setup(@Autowired UserRepository userRepository, @Autowired AddressRepository addressRepository
            , @Autowired CommercialProposalRepository commercialProposalRepository, @Autowired PasswordEncoder passwordEncoder) {
        CommercialProposalEntity commercialProposal = new CommercialProposalEntity();
        commercialProposal.setName("Clean1");
        commercialProposal.setTime(Duration.ofMinutes(30));
        commercialProposal.setRequiredCountOfEmployees(2);
        commercialProposal.setPrice(100.0);
        commercialProposal.setType(ComercialProposalType.PER_AREA);
        commercialProposalRepository.save(commercialProposal);

        CommercialProposalEntity commercialProposal1 = new CommercialProposalEntity();
        commercialProposal1.setName("Clean2");
        commercialProposal1.setTime(Duration.ofHours(1));
        commercialProposal1.setRequiredCountOfEmployees(1);
        commercialProposal1.setPrice(100.0);
        commercialProposal1.setType(ComercialProposalType.PER_AREA);
        commercialProposalRepository.save(commercialProposal1);

        CommercialProposalEntity commercialProposal2 = new CommercialProposalEntity();
        commercialProposal2.setName("Clean3");
        commercialProposal2.setTime(Duration.ofMinutes(90));
        commercialProposal2.setRequiredCountOfEmployees(2);
        commercialProposal2.setPrice(100.0);
        commercialProposal2.setType(ComercialProposalType.PER_AREA);
        commercialProposalRepository.save(commercialProposal2);

        CommercialProposalEntity commercialProposal3 = new CommercialProposalEntity();
        commercialProposal3.setName("Clean4");
        commercialProposal3.setTime(Duration.ofMinutes(90));
        commercialProposal3.setRequiredCountOfEmployees(5);
        commercialProposal3.setPrice(100.0);
        commercialProposal3.setType(ComercialProposalType.PER_AREA);
        commercialProposalRepository.save(commercialProposal3);

        addUserToDB(passwordEncoder, userRepository, "user@gmail.com", "password", Role.USER);
        addUserToDB(passwordEncoder, userRepository, "user1@gmail.com", "password", Role.USER);
        addUserToDB(passwordEncoder, userRepository, "admin@gmail.com", "password", Role.ADMIN);
        addUserToDB(passwordEncoder, userRepository, "emp1@gmail.com", "password", Role.EMPLOYEE);
        addUserToDB(passwordEncoder, userRepository, "emp2@gmail.com", "password", Role.EMPLOYEE);
        addUserToDB(passwordEncoder, userRepository, "emp3@gmail.com", "password", Role.EMPLOYEE);
        addUserToDB(passwordEncoder, userRepository, "emp4@gmail.com", "password", Role.EMPLOYEE);
        addUserToDB(passwordEncoder, userRepository, "emp5@gmail.com", "password", Role.EMPLOYEE);
        addUserToDB(passwordEncoder, userRepository, "emp6@gmail.com", "password", Role.EMPLOYEE);
        addUserToDB(passwordEncoder, userRepository, "emp7@gmail.com", "password", Role.EMPLOYEE);

        restTemplate = new RestTemplate();
    }
}
