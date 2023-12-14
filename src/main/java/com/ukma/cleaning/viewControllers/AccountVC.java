package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountVC {
    private final UserService userService;

    //TODO
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account")
    String getAccount(Model model) {
        model.addAttribute("user", userService.getUser());
        return "account";
    }
}
