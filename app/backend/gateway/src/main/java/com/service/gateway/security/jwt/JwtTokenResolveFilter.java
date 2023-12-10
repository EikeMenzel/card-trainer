package com.service.gateway.security.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Component
public class JwtTokenResolveFilter implements WebFilter {
    private final JwtUtils jwtUtils;
    private final HashSet<String> excludedRoutes;
    public JwtTokenResolveFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        this.excludedRoutes = new HashSet<>();
        this.excludedRoutes.add("/api/v1/register");
        this.excludedRoutes.add("/api/v1/login");
        this.excludedRoutes.add("/api/v1/password/reset");

    }

    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        if(isRouteInExcludedList(exchange.getRequest().getURI().getPath()))
            return chain.filter(exchange); // Skip jwtTokenResolveFilter

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

    private boolean isRouteInExcludedList(String path) {
        var pathMatcher = new AntPathMatcher();
        // Check for exact match
        if (excludedRoutes.contains(path)) {
            return true;
        }
        // Handle pattern matching
        return pathMatcher.match("/api/v1/email/verify/*", path) || !path.startsWith("/api") || pathMatcher.match("/api/v1/decks/share/*", path);
    }
}
