package com.ukma.cleaning.address;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public void create(Long userId, AddressDto addressDto) {
        UserDto user = userService.getById(userId);
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        AddressEntity addressEntity = modelMapper.map(addressDto, AddressEntity.class);
        addressEntity.setUser(userEntity);
        addressRepository.save(addressEntity);
    }

    @Override
    public void update(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(addressDto.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("Can`t find address by id: " + addressDto.getId())
                );
        UserEntity user = addressEntity.getUser();
        addressRepository.delete(addressEntity);
        AddressEntity newAddress = modelMapper.map(addressDto, AddressEntity.class);
        newAddress.setId(null);
        newAddress.setUser(user);
        addressRepository.save(newAddress);
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressDto getById(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id).get();
        return modelMapper.map(addressEntity, AddressDto.class);
    }

    @Override
    public List<AddressDto> getByUserId(Long userId) {
        return addressRepository.findAddressEntitiesByUserId(userId).stream()
                .map(entity -> modelMapper.map(entity, AddressDto.class))
                .toList();
    }
}
