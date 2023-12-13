package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountVC {
    private final UserService userService;

    //TODO
    @GetMapping("/account")
    String getAccount(Model model) {
        model.addAttribute(
                "user",
                userService.getByEmail("m.burnatt@gmail.com")
        );
        return "account";
    }
}
