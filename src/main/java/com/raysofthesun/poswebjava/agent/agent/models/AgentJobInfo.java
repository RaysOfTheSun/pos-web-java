package com.raysofthesun.poswebjava.agent.agent.models;

import com.raysofthesun.poswebjava.agent.agent.constants.AgentLevel;
import lombok.Data;

@Data
public class AgentJobInfo {
	private AgentLevel level;
	private String superiorId;
}
