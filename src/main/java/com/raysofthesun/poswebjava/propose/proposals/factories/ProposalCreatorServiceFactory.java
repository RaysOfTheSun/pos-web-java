package com.raysofthesun.poswebjava.propose.proposals.factories;

import com.raysofthesun.poswebjava.core.common.factories.PosWebServiceFactory;
import com.raysofthesun.poswebjava.propose.proposals.services.core.ProposalCreatorService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProposalCreatorServiceFactory extends PosWebServiceFactory<ProposalCreatorService> {
    public ProposalCreatorServiceFactory(List<ProposalCreatorService> services) {
        super(services);
    }
}
