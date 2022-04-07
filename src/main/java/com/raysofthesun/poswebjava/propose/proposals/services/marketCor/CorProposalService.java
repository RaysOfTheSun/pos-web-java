package com.raysofthesun.poswebjava.propose.proposals.services.marketCor;

import com.raysofthesun.poswebjava.core.common.enums.Market;
import com.raysofthesun.poswebjava.propose.feign.application.ApplyApplicationApi;
import com.raysofthesun.poswebjava.propose.proposals.repositories.ProposalRepository;
import com.raysofthesun.poswebjava.propose.proposals.factories.ProposalCreatorServiceFactory;
import com.raysofthesun.poswebjava.propose.proposals.services.core.ProposalService;
import org.springframework.stereotype.Service;

@Service
public class CorProposalService extends ProposalService {
    public CorProposalService(ProposalRepository proposalRepository,
                              ApplyApplicationApi applyApplicationApi,
                              ProposalCreatorServiceFactory proposalCreatorServiceFactory) {
        super(proposalRepository, applyApplicationApi, proposalCreatorServiceFactory);
    }

    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
