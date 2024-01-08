package com.service.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
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

    @Value("${session-service.api.path}")
    private String sessionServiceUri;

    @Value("${frontend.path}")
    private String frontendServiceUri;


    //Services that just expose their documentation:
    @Value("${mail-service.api.path}")
    private String mailServiceUri;


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-service",
                        r -> r.path(
                                "/api/v1/register",
                                "/api/v1/login",
                                "/api/v1/email/verify/{token}",
                                "/api/v1/password",
                                "/api/v1/password/reset",

                                "/api/v1/auth/v3/api-docs"
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
                                "/api/v1/decks/{deckId}/histories",
                                "/api/v1/decks/{deckId}/histories/{historyId}",
                                "/api/v1/decks/{deckId}/export",
                                "/api/v1/decks/{deckId}/cards-to-learn",
                                "/api/v1/decks/{deckId}/cards",
                                "/api/v1/decks/{deckId}/cards/{cardsId}",
                                "/api/v1/decks/{deckId}/share",
                                "/api/v1/decks/share/{token}",
                                "/api/v1/images/{imageId}",
                                "/api/v1/images"
                        ).uri(cardsServiceUri)
                )

                .route("session-service",
                        r -> r.path(
                                "/api/v1/decks/{deckId}/learn-sessions",
                                "/api/v1/decks/{deckId}/next-card",
                                "/api/v1/learn-sessions/{learnSessionId}/rating",
                                "/api/v1/learn-sessions/{learnSessionId}/status",
                                "/api/v1/decks/{deckId}/peek-sessions",
                                "api/v1/peek-sessions/{peekSessionId}/next-card",
                                "/api/v1/peek-sessions/{peekSessionId}/cards/{cardId}",
                                "/api/v1/peek-sessions/{peekSessionId}/status"
                        ).uri(sessionServiceUri)
                )

                .route("achievement-service",
                        r -> r.path(
                                "/tmp/websocket/**"
                        ).uri("ws://localhost:8085"))

                // API-Services that the frontend can't reach, but to expose the documentation its necessary to expose the following stuff:

                .route("mail-service",
                        r -> r.path(
                                "/api/v1/mail/v3/api-docs"
                        ).uri(mailServiceUri)
                )



                // Fallback if a route was not found
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
