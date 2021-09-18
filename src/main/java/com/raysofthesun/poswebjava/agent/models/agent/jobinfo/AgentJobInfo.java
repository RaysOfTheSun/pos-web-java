package com.raysofthesun.poswebjava.agent.models.agent.jobinfo;

import com.raysofthesun.poswebjava.agent.constants.AgentLevel;
import lombok.Data;

@Data
public class AgentJobInfo {
	private AgentLevel level;
	private String superiorId;
}
