package com.service.gateway.security.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenResolveFilter implements WebFilter {
    private final JwtUtils jwtUtils;
    public JwtTokenResolveFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String jwt = jwtUtils.parseJwt(exchange);

        if (jwt == null || !jwtUtils.isJwtTokenValid(jwt)) {
            return chain.filter(exchange);
        }

        String userId = jwtUtils.getUserIdFromJwtToken(jwt);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("userId", userId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(mutatedExchange);
    }

}
