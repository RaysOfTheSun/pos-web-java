package com.raysofthesun.poswebjava.core.configuration.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "pos")
public class PosConfig {
    private List<PosProductInfo> products;
    private Map<String, PosDocumentRequirement> documentRequirements = new HashMap<>();
}
