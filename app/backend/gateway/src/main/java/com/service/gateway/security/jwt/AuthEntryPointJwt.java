package com.service.gateway.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements ServerAuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        logger.error("Unauthorized error: {}", e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("error", "Unauthorized");
        response.put("message", e.getMessage());
        response.put("path", exchange.getRequest().getPath().toString());

        return exchange.getResponse()
                .writeWith(Mono.fromSupplier(() -> {
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                    byte[] responseBytes;
                    try {
                        responseBytes = new ObjectMapper().writeValueAsBytes(response);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                    return exchange.getResponse().bufferFactory().wrap(responseBytes);
                }));
    }
}
