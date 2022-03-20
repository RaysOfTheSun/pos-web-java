package com.raysofthesun.poswebjava.apply2.insureds.mappers;

import com.raysofthesun.poswebjava.apply2.feign.models.ApiCustomer;
import com.raysofthesun.poswebjava.apply2.insureds.models.core.insured.Insured;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationInsuredMapper {
	ApplicationInsuredMapper MAPPER = Mappers.getMapper(ApplicationInsuredMapper.class);

	@Mapping(source = "id", target = "customerId")
	Insured mapApiCustomerToInsured(ApiCustomer apiCustomer);
}
