package com.ukma.cleaning_v2.address;

import com.ukma.cleaning_v2.user.dto.UserDto;

import java.util.List;

public interface AddressService {
//    void createAddress(UserDto user, Address address);
    void createAddress(long userId, AddressDto addressDto);
    void editAddress(AddressDto addressDto);
//    void editAddress(Address address);
    void deleteAddress(long id);
    boolean hasAttachedOrders(long id);
    AddressDto getAddress(long id);
//    AddressDto getAddressDto(long id);
//    List<Address> getUserAddresses(User user);
    List<AddressDto> getUserAddresses(UserDto userDto);
    List<AddressDto> getAddressesByUserId(Long id);
}
