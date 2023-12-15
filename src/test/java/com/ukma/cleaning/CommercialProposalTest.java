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

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class CommercialProposalTest {

    @LocalServerPort
    private int port;

    @Test
    @Order(1)
    public void setup() {
        TestApplication.setupUsers();
    }
 
    @Test
    @Order(2)
    public void createGet(){
        TestTRT.authAs(1, port);
        var proposal = new CommercialProposalDto();
        proposal.setName("CommercialProposal1");
        proposal.setPrice(1000.0);
        proposal.setRequiredCountOfEmployees("2");
        proposal.setShortDescription("ShortDescription1");
        proposal.setFullDescription("FullDescription1");
        proposal.setType(ComercialProposalType.PER_AREA);

        var response = TestTRT.post("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
        assert(response.getStatusCode().is4xxClientError());

        proposal.setTime(Duration.ofHours(5));
        response = TestTRT.post("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
        assert(!response.getStatusCode().is4xxClientError());

        response = TestTRT.get("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
        assert(!response.getStatusCode().is4xxClientError());
        var byIdProposal = response.getBody();
        assert(proposal.getName().equals(byIdProposal.getName()));
        assert(proposal.getPrice().equals(byIdProposal.getPrice()));
        assert(proposal.getRequiredCountOfEmployees().equals(byIdProposal.getRequiredCountOfEmployees()));
        assert(proposal.getShortDescription().equals(byIdProposal.getShortDescription()));
        assert(proposal.getFullDescription().equals(byIdProposal.getFullDescription()));
        assert(proposal.getType().equals(byIdProposal.getType()));

        var responseList = TestTRT.get("http://localhost:" + port + "/api/commercial-proposals", List.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<CommercialProposalDto> proposals = (List<CommercialProposalDto>) responseList.getBody().stream()
            .map(obj -> mapper.convertValue(obj, CommercialProposalDto.class))
            .collect(Collectors.toList());
        assert(proposals.size() == 1);
        proposal.setId(1l);
        assert(proposals.get(0).equals(proposal));
    }

    @Test
    @Order(3)
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

        var response = TestTRT.put("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
        assert(!response.getStatusCode().is4xxClientError());

        response = TestTRT.get("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
        assert(!response.getStatusCode().is4xxClientError());
        var byIdProposal = response.getBody();
        assert(proposal.getName().equals(byIdProposal.getName()));
        assert(proposal.getPrice().equals(byIdProposal.getPrice()));
        assert(proposal.getRequiredCountOfEmployees().equals(byIdProposal.getRequiredCountOfEmployees()));
        assert(proposal.getShortDescription().equals(byIdProposal.getShortDescription()));
        assert(proposal.getFullDescription().equals(byIdProposal.getFullDescription()));
        assert(proposal.getType().equals(byIdProposal.getType()));
    }

    @Test
    @Order(4)
    public void delete(){
        TestTRT.delete("http://localhost:" + port + "/api/commercial-proposals/1");

        var responseGet = TestTRT.get("http://localhost:" + port + "/api/commercial-proposals/1", CommercialProposalDto.class);
        assert(responseGet.getStatusCode().is4xxClientError());

        var responseList = TestTRT.get("http://localhost:" + port + "/api/commercial-proposals", List.class);
        assert(!responseList.getStatusCode().is4xxClientError());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        var proposals = (List<CommercialProposalDto>) responseList.getBody().stream()
            .map(obj -> mapper.convertValue(obj, CommercialProposalDto.class))
            .collect(Collectors.toList());
        assert(proposals.size() == 0);
    }

    @Test
    @Order(5)
    public void userAccesses(){
        TestTRT.authAs(2, port);
        crudNoAccess();
    }

    @Test
    @Order(6)
    public void employeeAccesses(){
        TestTRT.authAs(3, port);
        crudNoAccess();
    }

    @Test
    @Order(7)
    @WithAnonymousUser
    public void anonymAccesses(){
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
            var response = TestTRT.post("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
            assert(response.getBody().getName() == null);
        } catch (Exception e) {}
        

        proposal.setName("CommercialProposal6");
        proposal.setId(1l);
        try {
            var response = TestTRT.put("http://localhost:" + port + "/api/commercial-proposals", proposal, CommercialProposalDto.class);
            assert(response.getBody().getName() == null || response.getBody().getName().equals("CommercialProposal5"));
        } catch (Exception e) {}
        

        TestTRT.delete("http://localhost:" + port + "/api/commercial-proposals/1");

        var responseList = TestTRT.get("http://localhost:" + port + "/api/commercial-proposals", List.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        responseList.getBody().stream()
            .map(obj -> mapper.convertValue(obj, CommercialProposalDto.class))
            .collect(Collectors.toList());

        assert(responseList.getStatusCode().is2xxSuccessful());
    }
    
}
