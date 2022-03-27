package com.raysofthesun.poswebjava.apply.feign;

import com.raysofthesun.poswebjava.apply.feign.models.ApiCustomer;
import com.raysofthesun.poswebjava.core.enums.Market;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface CustomerApi {

	@GetMapping("{market}/{agentId}/customers/customer")
	Flux<ApiCustomer> getCustomersByIdAndAgentId(@PathVariable("agentId") String agentId,
												 @RequestParam("ids") Collection<String> ids,
												 @PathVariable("market")Market market);
}
