package com.ukma.cleaning.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupVC {

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }
}
