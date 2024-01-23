package com.service.gateway.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;
import java.util.Optional;

@Component
public class JwtUtils {
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookieName;

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public boolean isJwtTokenValid(String authenticationToken) {
        if(authenticationToken != null && authenticationToken.chars().filter(ch -> ch == '.').count() != 2) // Validity check. JWT-Tokens need to contain two dots.
            return false;

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(authenticationToken);
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    public String parseJwt(ServerWebExchange exchange) {
        Optional<HttpCookie> jwtCookie = Optional.ofNullable(
                exchange.getRequest().getCookies().getFirst(jwtCookieName)
        );
        String token =  jwtCookie.map(HttpCookie::getValue).orElse(null);

        // If the JWT was not found in the cookie, try to get it from a query parameter - necessary for Websockets
        if(token == null) {
            token = exchange.getRequest().getQueryParams().getFirst("jwt-token");
        }
        return token;
    }
}
