package com.ukma.cleaning.commercialProposal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commercial-proposals")
@RequiredArgsConstructor
@Tag(name = "Commercial Proposal API", description = "Endpoint for operations with commercial proposal")
public class CommercialProposalController {
    private final CommercialProposalService commercialProposalService;

    @Operation(summary = "Get proposal by id", description = "Get proposal by id")
    @GetMapping("/{id}")
    public CommercialProposalDto getProposal(@PathVariable Long id) {
        return commercialProposalService.getById(id);
    }

    @Operation(summary = "Get all proposals", description = "Get all proposals")
    @GetMapping
    public List<CommercialProposalDto> getAllProposals() {
        return commercialProposalService.getAll();
    }

    @Operation(summary = "Edit proposal", description = "Edit proposal")
    @PutMapping
    public CommercialProposalDto editProposal(@RequestBody CommercialProposalDto proposalDto) {
        return commercialProposalService.update(proposalDto);
    }

    @Operation(summary = "Create proposal", description = "Create proposal")
    @PostMapping
    public CommercialProposalDto createAddress(@RequestBody CommercialProposalDto addressDto) {
        return commercialProposalService.create(addressDto);
    }

    @Operation(summary = "Delete commercial proposal", description = "Delete commercial proposal(soft delete)")
    @DeleteMapping("/{id}")
    public Boolean deleteAddress(@PathVariable Long id) {
        return commercialProposalService.deleteById(id);
    }
}
