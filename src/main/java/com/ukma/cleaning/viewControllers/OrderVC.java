package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderVC {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/order")
    public String showOrder(Model model) {
        model.addAttribute("user", userService.getUser());
        return "order/order";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/order/success")
    public String showOrderSuccess() {
        return "order/order-success";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("user", userService.getUser());
        return "order/orders";
    }

}
