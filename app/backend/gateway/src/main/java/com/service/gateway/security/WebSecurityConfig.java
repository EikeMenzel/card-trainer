package com.service.gateway.security;

import com.service.gateway.security.jwt.AuthEntryPointJwt;
import com.service.gateway.security.jwt.AuthTokenFilter;
import com.service.gateway.security.jwt.JwtTokenResolveFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authTokenFilter;
    private final JwtTokenResolveFilter jwtTokenResolveFilter;
    public WebSecurityConfig(AuthEntryPointJwt authEntryPointJwt, AuthTokenFilter authTokenFilter, JwtTokenResolveFilter jwtTokenResolveFilter) {
        this.authEntryPointJwt = authEntryPointJwt;
        this.authTokenFilter = authTokenFilter;
        this.jwtTokenResolveFilter = jwtTokenResolveFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .exceptionHandling(spec -> spec
                        .authenticationEntryPoint(authEntryPointJwt))
                .authorizeExchange(spec -> spec
                        .pathMatchers(
                                "/api/v1/register",
                                "/api/v1/login",
                                "/api/v1/email/verify/{token}",
                                "/api/v1/password/reset"
                        ).permitAll()
                        .pathMatchers("/api/**")
                        .authenticated()
                        .anyExchange().permitAll())
                .addFilterAfter(authTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(jwtTokenResolveFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
