package com.ukma.cleaning.user;

import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import com.ukma.cleaning.utils.NoSuchEntityException;
import com.ukma.cleaning.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserRegistrationDto user) {
        return mapper.toDto(userRepository.save(mapper.toEntity(user, encoder)));
    }

    @Override
    public UserDto update(UserDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(
                () -> new NoSuchEntityException("Can`t find user by id: " + user.getId())
        );
        mapper.updateFields(userEntity, user);
        return mapper.toDto(userRepository.save(userEntity));
    }
    @Override
    public Boolean deleteById(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public UserDto getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("Can`t find user by id: " + id)
        );
        return mapper.toDto(userEntity);
    }

    @Override
    public UserDto getByEmail(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(
                () -> new NoSuchEntityException("Can`t find user by email: " + email)
        );
        return mapper.toDto(userEntity);
    }

    @Override
    public UserDto updatePassword(UserPasswordDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(
                () -> new NoSuchEntityException("Can`t find user by id: " + user.getId())
        );
        userEntity.setPassword(encoder.encode(user.getPassword()));
        return mapper.toDto(userRepository.save(userEntity));
    }
}
