package com.ukma.cleaning.viewControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EmploymentFormVC {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/employment-form")
    String getApplicants() {
        return "employment-form";
    }
}
