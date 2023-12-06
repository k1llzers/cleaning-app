package com.ukma.cleaning.address;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserService;
import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.utils.mappers.AddressMapper;
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
    private final AddressMapper addressMapper;

    @Override
    public AddressDto create(Long userId, AddressDto addressDto) {
        UserDto user = userService.getById(userId);
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        AddressEntity addressEntity = addressMapper.toEntity(addressDto);
        addressEntity.setUser(userEntity);
        return addressMapper.toDto(addressEntity);
    }

    @Override
    public AddressDto update(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(addressDto.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("Can`t find address by id: " + addressDto.getId())
                );
        UserEntity user = addressEntity.getUser();
        addressRepository.delete(addressEntity);
        AddressEntity newAddress = addressMapper.toEntity(addressDto);
        newAddress.setId(null);
        newAddress.setUser(user);
        addressRepository.save(newAddress);
        return addressMapper.toDto(newAddress);
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressDto getById(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id).get();
        return addressMapper.toDto(addressEntity);
    }

    @Override
    public List<AddressDto> getByUserId(Long userId) {
        return addressMapper.toListDto(addressRepository.findAddressEntitiesByUserId(userId));
    }
}
