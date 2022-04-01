package com.raysofthesun.poswebjava;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "pos-feign")
public class FeignConfig {
	private String agentServiceBaseUrl;
	private String applyServiceBaseUrl;
}
