package com.raysofthesun.poswebjava.propose.proposals.services.marketCor;

import com.raysofthesun.poswebjava.core.enums.Market;
import com.raysofthesun.poswebjava.propose.proposals.services.core.ProposalCreatorService;
import org.springframework.stereotype.Service;

@Service
public class CorProposalCreatorService extends ProposalCreatorService {
    @Override
    public Market getMarket() {
        return Market.COR;
    }
}
