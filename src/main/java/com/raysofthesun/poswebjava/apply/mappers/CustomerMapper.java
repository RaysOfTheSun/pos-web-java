package com.raysofthesun.poswebjava.apply.mappers;

import com.raysofthesun.poswebjava.apply.feign_clients.agent.models.Customer;
import com.raysofthesun.poswebjava.apply.models.insured.Insured;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
	CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

	@Mapping(source = "id", target = "customerId")
	Insured mapCustomerToInsured(Customer customer);
}
