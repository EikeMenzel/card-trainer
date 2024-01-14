package com.service.achievementservice.configuration;

import com.service.achievementservice.services.WebSocketService;
import com.sun.security.auth.UserPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@SuppressWarnings("java:S1191") // suppresses warning for using Sun proprietary API
public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger loggerUserHandShakeHandler = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        final var randomId = UUID.randomUUID().toString();
        loggerUserHandShakeHandler.debug("User with ID '{}' opened the page", randomId);

        var userId = Long.valueOf(Objects.requireNonNull(request.getHeaders().getFirst("userId")));
        WebSocketService.addSocketMapping(userId, randomId);
        return new UserPrincipal(randomId);
    }
}