package com.ukma.cleaning.viewControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartVC {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/cart")
    public String showCart() {
        return "cart";
    }

}
