package com.raysofthesun.poswebjava.apply.insureds.mappers;

import com.raysofthesun.poswebjava.apply.feign.models.ApiCustomer;
import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface ApplicationInsuredMapper {
	ApplicationInsuredMapper MAPPER = Mappers.getMapper(ApplicationInsuredMapper.class);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "id", target = "customerId")
	Insured mapApiCustomerToInsured(ApiCustomer apiCustomer);
}
