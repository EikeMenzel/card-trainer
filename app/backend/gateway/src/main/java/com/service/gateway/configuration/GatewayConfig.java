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

    @Value("${frontend.path}")
    private String frontendServiceUri;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-service",
                        r -> r.path(
                                "/api/v1/register",
                                "/api/v1/login",
                                "/api/v1/email/verify/{token}",
                                "/api/v1/password",
                                "/api/v1/password/reset"
                        ).uri(authenticationServiceUri))

                .route("user-service",
                        r -> r.path(
                                "/api/v1/account"
                        ).uri(userServiceUri)
                )

                .route("cards-service",
                        r -> r.path(
                                "/api/v1/decks",
                                "/api/v1/decks/import",
                                "/api/v1/decks/{deckId}",
                                "/api/v1/histories",
                                "/api/v1/histories/{historyId}",
                                "/api/v1/decks/{deckId}/export",
                                "/api/v1/decks/{deckId}/cards",
                                "/api/v1/decks/{deckId}/cards/{cardsId}"
                        ).uri(cardsServiceUri)
                )

                .route("fallback",
                        r -> r.path("/api/**").filters(f -> f.setStatus(HttpStatus.NOT_FOUND))
                                .uri("no://op"))

                .route("frontend",
                        r -> r.path("/**")
                        .and().not(predicateSpec -> predicateSpec.path("/api/**"))
                        .uri(frontendServiceUri))

                .build();
    }
}
