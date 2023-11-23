package com.service.authenticationservice.security.jwt;

import com.service.authenticationservice.controller.AuthController;
import com.service.authenticationservice.security.services.UserDetailsServiceImpl;
import com.service.authenticationservice.services.DbQueryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private DbQueryService dbQueryService;
    private final Logger loggerAuthFilter =  LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt) && dbQueryService.getVerificationStateUser(Long.valueOf(jwtUtils.getUserIdFromJwtToken(jwt))).isPresent()) {
                Optional<String> optionalEmail = dbQueryService.getUserEmailFromId(Long.valueOf(jwtUtils.getUserIdFromJwtToken(jwt)));

                if(optionalEmail.isPresent()) {
                    var userDetails = userDetailsService.loadUserByUsername(optionalEmail.get());
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            loggerAuthFilter.error("Cannot set user authentication:", e);
        }
        filterChain.doFilter(request, response);
    }
}
