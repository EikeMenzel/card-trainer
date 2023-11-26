package com.service.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class GatewayConfig {
    @Value("${auth-service.api.path}")
    private String authenticationServiceUri;

    @Value("${user-service.api.path}")
    private String userServiceUri;

    @Value("${cards-service.api.path}")
    private String cardsServiceUri;
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-service",
                        r -> r.path(
                                "/api/v1/register",
                                "/api/v1/login",
                                "/api/v1/email/verify/{token}"
                        ).uri(authenticationServiceUri))

                .route("user-service",
                        r -> r.path(
                                "/api/v1/account"
                        ).uri(userServiceUri)
                )

                .route("cards-service",
                        r -> r.path(
                                "/api/v1/decks",
                                "/api/v1/decks/{deckId}"
                        ).uri(cardsServiceUri)
                )

                .route("fallback-route", r -> r.path("/**")
                        .filters(f -> f.setStatus(HttpStatus.NOT_FOUND))
                        .uri("no://op"))
                .build();
    }
}
