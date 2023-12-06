package com.ukma.cleaning.commercialProposal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class CommercialProposalServiceImpl implements CommercialProposalService {
    private final CommercialProposalRepository commercialProposalRepository;
    @Override
    public CommercialProposalDto create(CommercialProposalDto commercialProposal) {

        return null;
    }

    @Override
    public CommercialProposalDto update(CommercialProposalDto commercialProposal) {
        return null;
    }

    @Override
    public CommercialProposalDto getById(Long id) {
        return null;
    }

    @Override
    public List<CommercialProposalDto> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
