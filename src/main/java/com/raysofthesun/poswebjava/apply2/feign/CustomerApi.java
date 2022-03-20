package com.raysofthesun.poswebjava.apply2.feign;

import com.raysofthesun.poswebjava.apply2.feign.models.ApiCustomer;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface CustomerApi {

	@RequestLine("GET /{agentId}/customers/customer")
	Flux<ApiCustomer> getCustomersByIdAndAgentId(@Param("agentId") String agentId,
												 @Param("ids") Collection<String> ids);
}
