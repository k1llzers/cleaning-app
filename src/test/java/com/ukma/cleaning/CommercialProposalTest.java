package com.ukma.cleaning;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.ukma.cleaning.commercialProposal.ComercialProposalType;
import com.ukma.cleaning.commercialProposal.CommercialProposalDto;
import com.ukma.cleaning.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class CommercialProposalTest {

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
    public void createGet(){
        var proposal = new CommercialProposalDto();
        proposal.setName("CommercialProposal1");
        proposal.setPrice(1000.0);
        proposal.setRequiredCountOfEmployees("2");
        proposal.setShortDescription("ShortDescription1");
        proposal.setFullDescription("FullDescription1");
        proposal.setType(ComercialProposalType.PER_AREA);

        try {
            restTemplate.postForObject("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
            assert(false);
        } catch (Exception e) {}
        proposal.setTime(Duration.ofHours(5));
        restTemplate.postForObject("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);

        var byIdProposal = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
        assert(proposal.getName().equals(byIdProposal.getName()));
        assert(proposal.getPrice().equals(byIdProposal.getPrice()));
        assert(proposal.getRequiredCountOfEmployees().equals(byIdProposal.getRequiredCountOfEmployees()));
        assert(proposal.getShortDescription().equals(byIdProposal.getShortDescription()));
        assert(proposal.getFullDescription().equals(byIdProposal.getFullDescription()));
        assert(proposal.getType().equals(byIdProposal.getType()));

        var response = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals", List.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<CommercialProposalDto> proposals = (List<CommercialProposalDto>) response.stream()
            .map(obj -> mapper.convertValue(obj, CommercialProposalDto.class))
            .collect(Collectors.toList());
        assert(proposals.size() == 1);
        proposal.setId(1l);
        assert(proposals.get(0).equals(proposal));
    }

    @Test
    @Order(2)
    public void update(){
        var proposal = new CommercialProposalDto();
        proposal.setName("CommercialProposal2");
        proposal.setPrice(1000.0);
        proposal.setRequiredCountOfEmployees("2");
        proposal.setShortDescription("ShortDescription2");
        proposal.setFullDescription("FullDescription2");
        proposal.setType(ComercialProposalType.PER_AREA);
        proposal.setTime(Duration.ofHours(5));
        proposal.setId(1l);

        restTemplate.put("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);

        var byIdProposal = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
        assert(proposal.getName().equals(byIdProposal.getName()));
        assert(proposal.getPrice().equals(byIdProposal.getPrice()));
        assert(proposal.getRequiredCountOfEmployees().equals(byIdProposal.getRequiredCountOfEmployees()));
        assert(proposal.getShortDescription().equals(byIdProposal.getShortDescription()));
        assert(proposal.getFullDescription().equals(byIdProposal.getFullDescription()));
        assert(proposal.getType().equals(byIdProposal.getType()));
    }

    @Test
    @Order(3)
    public void delete(){
        restTemplate.delete("http://localhost:" + port + "/api/commercial-proposals/1");
        try {
            restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
            assert(false);
        } catch (Exception e) {}
        var proposals = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals", List.class);
        assert(proposals.size() == 0);
    }



    @Test
    @Order(4)
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void crudAdminAccess()
    {
        var proposal = new CommercialProposalDto();
        proposal.setName("CommercialProposal3");
        proposal.setPrice(1000.0);
        proposal.setRequiredCountOfEmployees("2");
        proposal.setShortDescription("ShortDescription3");
        proposal.setFullDescription("FullDescription3");
        proposal.setType(ComercialProposalType.PER_AREA);
        proposal.setTime(Duration.ofHours(5));

        var createdProposal = restTemplate.postForObject("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
        assert(createdProposal.getId() != null);

        proposal.setName("CommercialProposal4");
        proposal.setPrice(2000.0);
        proposal.setRequiredCountOfEmployees("3");
        proposal.setShortDescription("ShortDescription4");
        proposal.setFullDescription("FullDescription4");
        proposal.setType(ComercialProposalType.PER_ITEM);
        proposal.setTime(Duration.ofHours(6));
        proposal.setId(createdProposal.getId());

        restTemplate.put("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
        var updatedProposal = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals/" + createdProposal.getId(), CommercialProposalDto.class);

        assert(updatedProposal.getId().equals(proposal.getId()));
        assert(updatedProposal.getName().equals(proposal.getName()));
        assert(updatedProposal.getPrice().equals(proposal.getPrice()));
        assert(updatedProposal.getRequiredCountOfEmployees().equals(proposal.getRequiredCountOfEmployees()));
        assert(updatedProposal.getShortDescription().equals(proposal.getShortDescription()));
        assert(updatedProposal.getFullDescription().equals(proposal.getFullDescription()));
        assert(updatedProposal.getType().equals(proposal.getType()));
        assert(updatedProposal.getTime().equals(proposal.getTime()));

        var byIdProposal = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals/" + createdProposal.getId(), CommercialProposalDto.class);
        assert(byIdProposal.getId().equals(createdProposal.getId()));
        assert(byIdProposal.getName().equals(proposal.getName()));
        assert(byIdProposal.getPrice().equals(proposal.getPrice()));
        assert(byIdProposal.getRequiredCountOfEmployees().equals(proposal.getRequiredCountOfEmployees()));
        assert(byIdProposal.getShortDescription().equals(proposal.getShortDescription()));
        assert(byIdProposal.getFullDescription().equals(proposal.getFullDescription()));
        assert(byIdProposal.getType().equals(proposal.getType()));
        assert(byIdProposal.getTime().equals(proposal.getTime()));

        var response = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals", List.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<CommercialProposalDto> proposals = (List<CommercialProposalDto>) response.stream()
            .map(obj -> mapper.convertValue(obj, CommercialProposalDto.class))
            .collect(Collectors.toList());
        assert(proposals.size() == 1);
        assert(proposals.get(0).equals(proposal));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "user2", password = "User2")
    public void crudUserNoAccess(){
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
    @WithAnonymousUser
    public void crudAnonymNoAccess(){
        crudNoAccess();
    }


    public void crudNoAccess(){
        var proposal = new CommercialProposalDto();
        proposal.setName("CommercialProposal5");
        proposal.setPrice(555.0);
        proposal.setRequiredCountOfEmployees("5");
        proposal.setShortDescription("ShortDescription5");
        proposal.setFullDescription("FullDescription5");
        proposal.setType(ComercialProposalType.PER_AREA);
        proposal.setTime(Duration.ofHours(5));

        try {
            restTemplate.postForObject("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            proposal.setName("CommercialProposal6");
            proposal.setId(1l);
            restTemplate.put("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
            assert(false);
        } catch (Exception e) {}

        try {
            restTemplate.delete("http://localhost:" + port + "/api/commercial-proposals/1");
            assert(false);
        } catch (Exception e) {}

        try {
            restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
            var response = restTemplate.getForObject("http://localhost:" + port + "/api/commercial-proposals", List.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            response.stream()
                .map(obj -> mapper.convertValue(obj, CommercialProposalDto.class))
                .collect(Collectors.toList());
        } catch (Exception e) {
            assert(false);
        }
    }
    
}
