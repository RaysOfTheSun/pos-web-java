package com.raysofthesun.poswebjava.agent.controllers;

import com.raysofthesun.poswebjava.agent.services.AgentProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/agents")
public class AgentController {

    private final AgentProductService agentProductService;

    public AgentController(AgentProductService agentProductService) {
        this.agentProductService = agentProductService;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class KeycloakUser {
        private String username;
    }

    @AllArgsConstructor
    class TokenRequest {
        private String username;
        private String password;
        private String client_id;
        private String grant_type;

    }

    @GetMapping("/current-agent")
    public Mono<Map<String, Object>> getCurrentAgent(@AuthenticationPrincipal Jwt jwt) {
        return Mono.fromSupplier(() -> {
            return jwt.getClaims();
        });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Mono<Object> createAgent() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "admin");
        map.add("password", "admin");
        map.add("client_id", "admin-cli");
        map.add("grant_type", "password");

        return WebClient
                .create()
                .post()
                .uri("http://localhost:8080/auth/realms/master/protocol/openid-connect/token")
                .body(BodyInserters.fromPublisher(
                        Mono.just(map),
                        MultiValueMap.class
                ))
                .header("content-type", MediaType.APPLICATION_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(map1 -> {
                    return WebClient
                            .create()
                            .post()
                            .uri("http://localhost:8080/auth/admin/realms/pos-web/users")
                            .body(BodyInserters.fromPublisher(Mono.just(new KeycloakUser("rayosmlg02")), KeycloakUser.class))
                            .header("Authorization", "Bearer " + (String) map1.get("access_token"))
                            .retrieve()
                            .bodyToMono(Object.class)
                            .flatMap((a) -> {
                                return WebClient
                                        .create()
                                        .get()
                                        .uri("http://localhost:8080/auth/admin/realms/pos-web/users?username="+"rayosmlg02")
                                        .retrieve()
                                        .bodyToMono(List.class);
                            });
                });
    }
}
