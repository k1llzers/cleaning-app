package com.ukma.cleaning.viewControllers;

import com.ukma.cleaning.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AddressesVC {
    private final AddressService addressService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/addresses")
    String getAddresses(Model model) {
        model.addAttribute("addresses", addressService.getUserAddresses());
        return "addresses";
    }
}
