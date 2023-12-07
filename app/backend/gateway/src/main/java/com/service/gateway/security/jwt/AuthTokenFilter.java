package com.service.gateway.security.jwt;

import com.service.gateway.security.services.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
public class AuthTokenFilter implements WebFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String jwt = jwtUtils.parseJwt(exchange);

        if (jwt == null || !jwtUtils.isJwtTokenValid(jwt)) {
            return chain.filter(exchange);
        }

        return userDetailsService.findByUsername(jwtUtils.getUserIdFromJwtToken(jwt))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
                .flatMap(userDetails -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(
                                Mono.just(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                )))
                        )))
                .onErrorResume(UsernameNotFoundException.class, e -> {
                    if(exchange.getRequest().getURI().getPath().startsWith("/api")) {
                        exchange.getResponse().setStatusCode(HttpStatus.PRECONDITION_FAILED);
                        return exchange.getResponse().setComplete();
                    } else
                        return chain.filter(exchange);
                });
    }

}
