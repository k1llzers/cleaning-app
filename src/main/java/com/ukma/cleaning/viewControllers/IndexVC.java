package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.commercialProposal.CommercialProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexVC {
    private final CommercialProposalService commercialProposalService;

    @GetMapping()
    String getIndex(Model model) {
        model.addAttribute(
               "proposals",
                commercialProposalService.getAll()
        );
        return "index";
    }

}
