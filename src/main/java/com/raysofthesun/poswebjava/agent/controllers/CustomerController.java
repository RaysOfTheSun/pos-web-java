package com.raysofthesun.poswebjava.agent.controllers;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.services.CustomerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("v1/agents")
public class CustomerController {

	final protected CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PutMapping("/{agentId}/customers")
	public Mono<String> addCustomer(@PathVariable String agentId, @RequestBody Customer customer) {
		return customerService.addCustomerWithAgentId(customer, agentId);
	}

	@PatchMapping("/{agentId}/customers/{customerId}")
	public Mono<String> updateCustomer(@PathVariable String agentId, @PathVariable String customerId,
	                                   @RequestBody Customer customer) {
		return customerService.updateCustomer(customerId, agentId, customer);
	}

	@GetMapping("/{agentId}/customers")
	public Flux<Customer> getAllCustomersWithAgentId(@PathVariable String agentId) {
		return customerService.getAllCustomersWithAgentId(agentId);
	}

	@DeleteMapping("/{agentId}/customers/{customerId}")
	public Mono<Boolean> deleteCustomerByIdAndAgentId(@PathVariable String agentId, @PathVariable String customerId) {
		return customerService.deleteCustomerByIdAndAgentId(agentId, customerId);
	}

	@GetMapping("/{agentId}/customers/customer")
	public Flux<Customer> getCustomersWithIds(@PathVariable String agentId, @RequestParam List<String> ids) {
		return customerService.getCustomersByIdWithAgentId(agentId, ids);
	}
}
