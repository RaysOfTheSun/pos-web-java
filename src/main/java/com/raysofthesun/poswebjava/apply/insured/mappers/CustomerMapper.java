package com.raysofthesun.poswebjava.apply.insured.mappers;

import com.raysofthesun.poswebjava.apply.feign.customer.models.ApiCustomer;
import com.raysofthesun.poswebjava.apply.insured.models.insured.Insured;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
	CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

	@Mapping(source = "id", target = "customerId")
	Insured mapCustomerToInsured(ApiCustomer apiCustomer);
}
