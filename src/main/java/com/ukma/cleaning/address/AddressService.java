package com.ukma.cleaning.address;

import java.util.List;

public interface AddressService {
    void create(Long userId, AddressDto addressDto);
    void update(AddressDto addressDto);
    void deleteById(Long id);
    AddressDto getById(Long id);
    List<AddressDto> getByUserId(Long id);
}
