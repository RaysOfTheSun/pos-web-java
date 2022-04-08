package com.raysofthesun.poswebjava.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class EmptySecurityContext implements SecurityContext {
    @Override
    public Authentication getAuthentication() {
        return null;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
    }
}
