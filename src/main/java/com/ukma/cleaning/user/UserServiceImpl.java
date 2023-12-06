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
    public UserDto createUser(UserRegistrationDto user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setRole(Role.User);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserDto editUser(UserDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setPatronymic(user.getPatronymic());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }
    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUser(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
//        UserEntity userEntity = null;
//        try {
//            userEntity = userRepository.findUserEntityByEmail(email).get();
//        } catch (NoSuchElementException e) {
//            log.info("Can't find in db User with email: {} ",email);
//            return null;
//        }
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserPasswordDto getUserPassword(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserPasswordDto.class);
    }

    @Override
    public UserDto changePassword(UserPasswordDto user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setPassword(encoder.encode(user.getPassword()));
        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserPasswordDto getUserPasswordByEmail(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserPasswordDto.class);
    }
}
