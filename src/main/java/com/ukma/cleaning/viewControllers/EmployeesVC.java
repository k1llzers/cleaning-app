package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.employment.EmploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EmployeesVC {
    private final EmploymentService employmentService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/employees")
    public String showOrders(Model model) {
        model.addAttribute("applicants", employmentService.getAll());
        return "employees";
    }

}
