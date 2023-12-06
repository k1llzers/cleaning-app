package com.ukma.cleaning.address;

import java.util.List;

public interface AddressService {
    AddressDto create(Long userId, AddressDto addressDto);
    AddressDto update(AddressDto addressDto);
    void deleteById(Long id);
    AddressDto getById(Long id);
    List<AddressDto> getByUserId(Long id);
}
