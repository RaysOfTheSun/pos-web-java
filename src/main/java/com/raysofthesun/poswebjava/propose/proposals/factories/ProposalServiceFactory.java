package com.raysofthesun.poswebjava.propose.proposals.factories;

import com.raysofthesun.poswebjava.core.factories.PosWebServiceFactory;
import com.raysofthesun.poswebjava.propose.proposals.services.core.ProposalService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProposalServiceFactory extends PosWebServiceFactory<ProposalService> {
    public ProposalServiceFactory(List<ProposalService> services) {
        super(services);
    }
}
