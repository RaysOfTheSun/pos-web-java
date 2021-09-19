package com.raysofthesun.poswebjava.agent.services;

import com.raysofthesun.poswebjava.agent.repositories.AgentRepository;
import org.springframework.stereotype.Service;

@Service
public class AgentService {
	final protected AgentRepository agentRepository;

	public AgentService(AgentRepository agentRepository) {
		this.agentRepository = agentRepository;
	}
}
