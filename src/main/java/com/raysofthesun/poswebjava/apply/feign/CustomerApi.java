package com.raysofthesun.poswebjava.apply.feign;

import com.raysofthesun.poswebjava.apply.feign.models.ApiCustomer;
import com.raysofthesun.poswebjava.core.enums.Market;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface CustomerApi {

	@RequestLine("GET {market}/{agentId}/customers/customer")
	Flux<ApiCustomer> getCustomersByIdAndAgentId(@Param("agentId") String agentId,
												 @Param("ids") Collection<String> ids,
												 @Param("market")Market market);
}
