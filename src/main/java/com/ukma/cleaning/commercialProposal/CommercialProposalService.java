package com.ukma.cleaning.commercialProposal;

import com.ukma.cleaning.address.AddressDto;

import java.util.List;

public interface CommercialProposalService {
    CommercialProposalDto create(CommercialProposalDto commercialProposal);
    CommercialProposalDto update(CommercialProposalDto commercialProposal);
    CommercialProposalDto getById(Long id);
    List<CommercialProposalDto> getAll();
    void deleteById(Long id);
}
