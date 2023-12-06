package com.ukma.cleaning.commercialProposal;

import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommercialProposalServiceImpl implements CommercialProposalService {
    private final CommercialProposalRepository commercialProposalRepository;
    private final ModelMapper modelMapper;
    @Override
    public CommercialProposalDto create(CommercialProposalDto commercialProposal) {
        CommercialProposalEntity commercialProposalEntity = modelMapper.map(commercialProposal, CommercialProposalEntity.class);
        commercialProposalRepository.save(commercialProposalEntity);
        return modelMapper.map(commercialProposalEntity, CommercialProposalDto.class);
    }

    @Override
    public CommercialProposalDto update(CommercialProposalDto commercialProposal) {
        CommercialProposalEntity commercialProposalEntity = modelMapper.map(commercialProposal, CommercialProposalEntity.class);
        commercialProposalRepository.save(commercialProposalEntity);
        return modelMapper.map(commercialProposalEntity, CommercialProposalDto.class);
    }

    @Override
    public CommercialProposalDto getById(Long id) {
        CommercialProposalEntity entity = commercialProposalRepository.findById(id).orElseThrow(() -> new RuntimeException("Proposal not found"));
        return modelMapper.map(entity, CommercialProposalDto.class);
    }

    @Override
    public List<CommercialProposalDto> getAll() {
        List<CommercialProposalEntity> entities = commercialProposalRepository.findAll();
        return entities.stream()
                .map(entity -> modelMapper.map(entity, CommercialProposalDto.class))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        commercialProposalRepository.deleteById(id);
    }
}
