package com.raysofthesun.poswebjava.agent.controllers;

import com.raysofthesun.poswebjava.agent.services.AgentService;
import com.raysofthesun.poswebjava.agent.services.CustomerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agents")
public class AgentController {

	final protected AgentService agentService;

	public AgentController(AgentService agentService, CustomerService customerService) {
		this.agentService = agentService;
	}
}
