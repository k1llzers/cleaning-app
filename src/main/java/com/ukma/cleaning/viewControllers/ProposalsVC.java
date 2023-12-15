package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.commercialProposal.CommercialProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProposalsVC {
    private final CommercialProposalService proposalService;
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/proposals")
    String getProposals(Model model) {
        model.addAttribute("proposals", proposalService.getAll());
        return "proposals";
    }
}
