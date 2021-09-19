package com.raysofthesun.poswebjava.agent.controllers;

import com.raysofthesun.poswebjava.agent.models.customer.Customer;
import com.raysofthesun.poswebjava.agent.services.CustomerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/agents")
public class CustomerController {

	final protected CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PutMapping("/{agentId}/customers")
	public Mono<String> addCustomer(@PathVariable String agentId, @RequestBody Customer customer) {
		return customerService.addCustomerWithAgentId(customer, agentId);
	}

	@GetMapping("/{agentId}/customers")
	public Flux<Customer> getAllCustomersWithAgentId(@PathVariable String agentId) {
		return customerService.getAllCustomersWithAgentId(agentId);
	}

	@DeleteMapping("/{agentId}/customers/{customerId}")
	public Mono<Boolean> deleteCustomerByIdAndAgentId(@PathVariable String agentId, @PathVariable String customerId) {
		return customerService.deleteCustomerByIdAndAgentId(agentId, customerId);
	}
}
