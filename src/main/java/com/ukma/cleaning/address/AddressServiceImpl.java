package com.ukma.cleaning.address;

import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.mappers.AddressMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressDto create(Long userId, AddressDto addressDto) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("Can`t find address by id: " + userId)
        );
        AddressEntity addressEntity = addressMapper.toEntity(addressDto);
        addressEntity.setUser(userEntity);
        return addressMapper.toDto(addressRepository.save(addressEntity));
    }

    @Transactional
    @Override
    public AddressDto update(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findById(addressDto.getId()).orElseThrow(
                () -> new NoSuchElementException("Can`t find address by id: " + addressDto.getId())
        );
        UserEntity user = addressEntity.getUser();
        addressRepository.delete(addressEntity);
        AddressEntity newAddress = addressMapper.toEntity(addressDto);
        newAddress.setUser(user);
        return addressMapper.toDto(addressRepository.save(newAddress));
    }

    @Override
    public Boolean deleteById(Long id) {
        addressRepository.deleteById(id);
        return true;
    }

    @Override
    public AddressDto getById(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can`t find address by id: " + id)
        );
        return addressMapper.toDto(addressEntity);
    }

    @Override
    public List<AddressDto> getByUserId(Long userId) {
        return addressMapper.toListDto(addressRepository.findAddressEntitiesByUserId(userId));
    }
}
