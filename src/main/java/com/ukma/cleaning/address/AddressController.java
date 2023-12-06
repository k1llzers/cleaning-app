package com.ukma.cleaning.address;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@Tag(name = "Address API", description = "Endpoint for operations with addresses")
public class AddressController {
    public final AddressService addressService;

    @Operation(summary = "Get address by id", description = "Get address by id")
    @GetMapping("/{id}")
    public AddressDto getAddress(@PathVariable Long id) {
            return addressService.getAddress(id);
    }

    @Operation(summary = "Get all user addresses", description = "Get all user addresses")
    @GetMapping("/by-user/{userId}")
    public List<AddressDto> getUserAddresses(@PathVariable Long userId) {
        return addressService.getAddressesByUserId(userId);
    }

    @Operation(summary = "Change address", description = "Change address")
    @PutMapping()
    public void editAddress(@RequestBody AddressDto addressDto) {
        addressService.editAddress(addressDto);
    }

    @Operation(summary = "Create new address for user", description = "Create new address for user")
    @PostMapping("/{userId}")
    public void createAddress(@PathVariable Long userId, @RequestBody AddressDto addressDto) {
        addressService.createAddress(userId, addressDto);
    }

    @Operation(summary = "Delete address", description = "Delete address")
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
