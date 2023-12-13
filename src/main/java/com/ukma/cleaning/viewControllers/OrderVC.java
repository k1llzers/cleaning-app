package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderVC {
    private final UserService userService;

    @GetMapping("/order")
    public String showOrder(Model model) {
        model.addAttribute("user", userService.getByEmail("m.burnatt@gmail.com"));
        return "order/order";
    }

    @GetMapping("/order/success")
    public String showOrderSuccess() {
        return "order/order-success";
    }

}
