package com.ukma.cleaning.viewControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginVC {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
