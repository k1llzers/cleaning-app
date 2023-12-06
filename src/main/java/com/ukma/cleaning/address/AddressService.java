package com.ukma.cleaning.address;

import java.util.List;

public interface AddressService {
    void createAddress(Long userId, AddressDto addressDto);
    void editAddress(AddressDto addressDto);
    void deleteAddress(Long id);
    AddressDto getAddress(Long id);
    List<AddressDto> getAddressesByUserId(Long id);
}
