package com.ukma.cleaning.employment;

import com.ukma.cleaning.user.Role;
import com.ukma.cleaning.user.UserEntity;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.mappers.EmploymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentServiceImpl implements EmploymentService {
    private final EmploymentRepository repository;
    private final EmploymentMapper mapper;
    private final UserRepository userRepository;

    @Override
    public EmploymentDto create(String motivationList) {
        EmploymentEntity employmentRequest = mapper.toEntity(motivationList);
        String applicantEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity applicant = userRepository.findUserEntityByEmail(applicantEmail).orElseThrow(
                () -> new NoSuchEntityException("Cant find applicant by email: " + applicantEmail)
        );
        employmentRequest.setApplicant(applicant);
        return mapper.toDto(repository.save(employmentRequest));
    }

    @Transactional
    @Override
    public Boolean succeed(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchEntityException("Can`t find entity by id: " + userId)
        );
        EmploymentEntity employmentRequest = repository.findByApplicant_Id(userId).orElseThrow(
                () -> new NoSuchEntityException("Can`t find application by user id: " + userId)
        );
        repository.delete(employmentRequest);
        user.setAddressList(Collections.emptyList());
        user.setRole(Role.EMPLOYEE);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean cancel(Long userId) {
        EmploymentEntity employmentRequest = repository.findByApplicant_Id(userId).orElseThrow(
                () -> new NoSuchEntityException("Can`t find application by user id: " + userId)
        );
        repository.delete(employmentRequest);
        return true;
    }

    @Override
    public List<EmploymentDto> getAll() {
        return mapper.toDtoList(repository.findAll());
    }
}
