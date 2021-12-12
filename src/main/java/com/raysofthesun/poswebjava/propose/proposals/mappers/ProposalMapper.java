package com.raysofthesun.poswebjava.propose.proposals.mappers;

import com.raysofthesun.poswebjava.propose.feign.application.models.application.ApplicationCreationRequest;
import com.raysofthesun.poswebjava.propose.proposals.models.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProposalMapper {
	ProposalMapper PROPOSAL_MAPPER = Mappers.getMapper(ProposalMapper.class);

	@Mapping(source = "ownerId", target = "policyOwnerId")
	@Mapping(source = "insuredId", target = "primaryInsuredId")
	@Mapping(source = "type", target = "productType")
	ApplicationCreationRequest proposalToApplicationCreationRequest(Proposal proposal);
}
