package com.ukma.cleaning.user;

import com.ukma.cleaning.order.dto.OrderPageDto;
import com.ukma.cleaning.user.dto.UserDto;
import com.ukma.cleaning.user.dto.UserPageDto;
import com.ukma.cleaning.user.dto.UserPasswordDto;
import com.ukma.cleaning.user.dto.UserRegistrationDto;
import com.ukma.cleaning.utils.exceptions.EmailDuplicateException;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.exceptions.PhoneNumberDuplicateException;
import com.ukma.cleaning.utils.mappers.UserMapper;
import com.ukma.cleaning.utils.security.SecurityContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserRegistrationDto user) {
        if (userRepository.findUserEntityByEmail(user.getEmail()).isPresent()) {
            throw new EmailDuplicateException("Email already in use!");
        }
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user, encoder)));
    }

    @Override
    public UserDto update(UserDto user) {
        UserEntity userEntity = SecurityContextAccessor.getAuthenticatedUser();
        Optional<UserEntity> userEntityByEmail = userRepository.findUserEntityByEmail(user.getEmail());
        userEntityByEmail.ifPresent(existingUser -> {
            if (!Objects.equals(userEntity.getId(), existingUser.getId())) {
                throw new EmailDuplicateException("Email already in use!");
            }
        });
        Optional<UserEntity> userEntityByPhone = userRepository.findUserEntityByPhoneNumber(user.getPhoneNumber());
        userEntityByPhone.ifPresent(existingUser -> {
            if (!Objects.equals(userEntity.getId(), existingUser.getId())) {
                throw new PhoneNumberDuplicateException("Phone number already in use!");
            }
        });
        userMapper.updateFields(userEntity, user);
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public UserDto getUser() {
        return userMapper.toDto(SecurityContextAccessor.getAuthenticatedUser());
    }

    @Override
    public UserDto getByEmail(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(
                () -> new NoSuchEntityException("Can`t find user by email: " + email)
        );
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto updatePassword(UserPasswordDto user) {
        UserEntity userEntity = SecurityContextAccessor.getAuthenticatedUser();
        userEntity.setPassword(encoder.encode(user.getPassword()));
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public UserPageDto findUsersByPageAndRole(Role role, Pageable pageable) {
        Page<UserEntity> users = userRepository.findAllByRole(role, pageable);
        int totalPages = users.getTotalPages();
        return new UserPageDto(pageable.getPageNumber(), totalPages, userMapper.toUserListDto(users.stream().toList()));
    }
}
