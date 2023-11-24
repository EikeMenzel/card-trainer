package com.service.gateway.security.services;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final DbQueryService dbQueryService;

    public UserDetailsServiceImpl(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String userId) throws UsernameNotFoundException {
        return Mono.justOrEmpty(dbQueryService.getUserEmailFromId(Long.valueOf(userId)))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User Not Found with id: " + userId)))
                .flatMap(email -> Mono.justOrEmpty(dbQueryService.getUserByEmail(email)))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User Not Found with email")))
                .map(UserDetailsImpl::build);
    }
}
