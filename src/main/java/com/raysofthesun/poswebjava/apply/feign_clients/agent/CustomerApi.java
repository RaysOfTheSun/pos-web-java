package com.raysofthesun.poswebjava.apply.feign_clients.agent;

import com.raysofthesun.poswebjava.apply.feign_clients.agent.models.Customer;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface CustomerApi {

	@RequestLine("GET /{agentId}/customers/customer")
	Flux<Customer> getCustomersByIdAndAgentId(@Param("agentId") String agentId,
	                                          @Param("ids") Collection<String> ids);
}
