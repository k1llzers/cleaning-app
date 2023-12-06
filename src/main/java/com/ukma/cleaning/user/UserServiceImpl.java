package com.ukma.cleaning.user;

import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto create(UserRegistrationDto user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setRole(Role.User);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserDto update(UserDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setPatronymic(user.getPatronymic());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getByEmail(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserPasswordDto getPasswordById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserPasswordDto.class);
    }

    @Override
    public UserDto updatePassword(UserPasswordDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setPassword(encoder.encode(user.getPassword()));
        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserPasswordDto getPasswordByEmail(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserPasswordDto.class);
    }
}
