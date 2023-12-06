package com.ukma.cleaning.commercialProposal;

import com.ukma.cleaning.utils.mappers.CommercialProposalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommercialProposalServiceImpl implements CommercialProposalService {
    private final CommercialProposalRepository commercialProposalRepository;
    private final CommercialProposalMapper mapper;
    @Override
    public CommercialProposalDto create(CommercialProposalDto commercialProposal) {
        return mapper.toDto(commercialProposalRepository.save(mapper.toEntity(commercialProposal)));
    }

    @Override
    public CommercialProposalDto update(CommercialProposalDto commercialProposal) {
        return mapper.toDto(commercialProposalRepository.save(mapper.toEntity(commercialProposal)));
    }

    @Override
    public CommercialProposalDto getById(Long id) {
        CommercialProposalEntity entity = commercialProposalRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Proposal not found")
        );
        return mapper.toDto(entity);
    }

    @Override
    public List<CommercialProposalDto> getAll() {
        return mapper.toDtoList(commercialProposalRepository.findAll());
    }

    @Override
    public Boolean deleteById(Long id) {
        commercialProposalRepository.deleteById(id);
        return true;
    }
}
