package com.ukma.cleaning.commercialProposal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commercial-proposals")
@RequiredArgsConstructor
public class CommercialProposalController {
    private final CommercialProposalService commercialProposalService;

    @GetMapping("/{id}")
    public CommercialProposalDto getProposal(@PathVariable Long id) {
        return commercialProposalService.getById(id);
    }

    @GetMapping
    public List<CommercialProposalDto> getAllProposals() {
        return commercialProposalService.getAll();
    }

    @PutMapping
    public CommercialProposalDto editProposal(@RequestBody CommercialProposalDto proposalDto) {
        return commercialProposalService.update(proposalDto);
    }

    @PostMapping
    public CommercialProposalDto createAddress(@RequestBody CommercialProposalDto addressDto) {
        return commercialProposalService.create(addressDto);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteAddress(@PathVariable Long id) {
        return commercialProposalService.deleteById(id);
    }
}
