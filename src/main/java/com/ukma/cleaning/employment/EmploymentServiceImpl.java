package com.ukma.cleaning.employment;

import com.ukma.cleaning.user.Role;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.exceptions.AlreadyAppliedException;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.mappers.EmploymentMapper;
import com.ukma.cleaning.utils.security.SecurityContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmploymentServiceImpl implements EmploymentService {
    private final EmploymentRepository repository;
    private final EmploymentMapper mapper;
    private final UserRepository userRepository;

    @Override
    public EmploymentDto create(String motivationList) {
        if (repository.findByApplicant_Id(SecurityContextAccessor.getAuthenticatedUserId()).isPresent()) {
            log.info("User id = {} try to send more than 1 application for a job", SecurityContextAccessor.getAuthenticatedUserId());
            throw new AlreadyAppliedException("You have already applied for this position");
        }
        EmploymentEntity employmentRequest = mapper.toEntity(motivationList);
        employmentRequest.setApplicant(SecurityContextAccessor.getAuthenticatedUser());
        log.info("Created new employment request with id = {}", employmentRequest.getId());
        return mapper.toDto(repository.save(employmentRequest));
    }

    @Transactional
    @Override
    public Boolean succeed(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Can`t find user by id = {}", userId);
            return new NoSuchEntityException("Can`t find user by id: " + userId);
        });
        EmploymentEntity employmentRequest = repository.findByApplicant_Id(userId).orElseThrow(() -> {
            log.info("Can`t find application by user id = {}", userId);
            return new NoSuchEntityException("Can`t find application by user id: " + userId);
        });
        repository.delete(employmentRequest);
        user.setAddressList(Collections.emptyList());
        user.setRole(Role.EMPLOYEE);
        userRepository.save(user);
        log.debug("Admin id = {} accepted Employment request id = {}",
                SecurityContextAccessor.getAuthenticatedUserId(), employmentRequest.getId());
        return true;
    }

    @Override
    public Boolean cancel(Long userId) {
        EmploymentEntity employmentRequest = repository.findByApplicant_Id(userId).orElseThrow(() -> {
            log.info("Can`t find application by user id = {}", userId);
            return new NoSuchEntityException("Can`t find application by user id: " + userId);
        });
        repository.delete(employmentRequest);
        log.debug("Admin id = {} cancelled Employment request id = {}",
                SecurityContextAccessor.getAuthenticatedUserId(), employmentRequest.getId());
        return true;
    }

    @Override
    public List<EmploymentDto> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public Boolean unemployment(Long userId) {
        UserEntity employee = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Can`t find user by id = {}", userId);
            return new NoSuchEntityException("Can`t find user by id: " + userId);
        });
        employee.setRole(Role.USER);
        userRepository.save(employee);
        log.info("User id = {} was fired by Admin id = {}", userId,
                SecurityContextAccessor.getAuthenticatedUserId());
        return true;
    }
}
