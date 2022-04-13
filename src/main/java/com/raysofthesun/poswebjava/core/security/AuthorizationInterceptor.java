package com.raysofthesun.poswebjava.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthorizationInterceptor implements ReactiveHttpRequestInterceptor {

    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest reactiveHttpRequest) {
        return Mono
                .just(reactiveHttpRequest)
                .transformDeferredContextual((reactiveHttpRequestMono, contextView) -> {
                    if (!contextView.hasKey(SecurityContext.class)) {
                        return reactiveHttpRequestMono;
                    }

                    Mono<SecurityContext> securityContextMono = contextView
                            .<Mono<SecurityContext>>get(SecurityContext.class);

                    return securityContextMono
                            .defaultIfEmpty(new EmptySecurityContext())
                            .map(securityContext -> {
                                Authentication authentication = securityContext.getAuthentication();

                                if (authentication == null) {
                                    return reactiveHttpRequest;
                                }

                                Jwt jwt = (Jwt) authentication.getCredentials();
                                String authHeaderVal = String.format("Bearer %s", jwt.getTokenValue());

                                reactiveHttpRequest.headers().put("Authorization", List.of(authHeaderVal));

                                return reactiveHttpRequest;
                            });
                });
    }
}
