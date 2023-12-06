package com.ukma.cleaning.address;

import com.ukma.cleaning.order.OrderRepository;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OrderRepository orderRepository;

    @Override
    public void createAddress(long userId, AddressDto addressDto) {
        UserDto user = userService.getUser(userId);
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        AddressEntity addressEntity = modelMapper.map(addressDto, AddressEntity.class);
        addressEntity.setUser(userEntity);
        addressRepository.save(addressEntity);
//        Address address = modelMapper.map(addressDto, Address.class);
//        createAddress(user, address);
    }

    @Override
    public void editAddress(AddressDto addressDto) {
        AddressEntity addressEntity = modelMapper.map(addressDto, AddressEntity.class);
        addressRepository.save(addressEntity); // ???
//        AddressEntity addressEntity = addressRepository.findById(addressDto.getId()).get();
//        addressEntity.setCity(addressDto.getCity());
//        addressEntity.setStreet(addressDto.getStreet());
//        addressEntity.setHouseNumber(addressDto.getHouseNumber());
//        addressEntity.setFlatNumber(addressDto.getFlatNumber());
//        addressEntity.setZip(addressDto.getZip());
    }

    @Override
    public void deleteAddress(long id) {
        AddressEntity addressEntity = addressRepository.findById(id).get();
        addressEntity.setDeleted(true);
        addressRepository.save(addressEntity);
    }

    @Override
    public AddressDto getAddress(long id) {
        AddressEntity addressEntity = addressRepository.findById(id).get();
        return modelMapper.map(addressEntity, AddressDto.class);
    }

    @Override
    public List<AddressDto> getUserAddresses(UserDto userDto) {
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        List<AddressEntity> addresses = addressRepository.findAddressEntitiesByUser(userEntity);
        return addresses.stream().map(x -> modelMapper.map(x, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> getAddressesByUserId(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(e -> e + "").toList();
        UserDto userDto = userService.getUser(id);
        if (!authorities.contains("ROLE_Admin") && !userDto.getEmail().equals(authentication.getName())){
            throw new AccessDeniedException("You can`t get addresses by id " + id);
        }
        return getUserAddresses(userDto);
    }

    @Override
    public boolean hasAttachedOrders(long id) {
//        Address address = getAddress(id);
        return false;
        //return address.getOrders().stream().anyMatch(x -> x.getOrderStatus() != Status.NOT_VERIFIED && x.getOrderStatus() != Status.CANCELLED);
    }
}
