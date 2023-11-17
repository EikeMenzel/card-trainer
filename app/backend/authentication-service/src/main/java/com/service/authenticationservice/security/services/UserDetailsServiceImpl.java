package com.service.authenticationservice.security.services;

import com.service.authenticationservice.payload.inc.UserDTO;
import com.service.authenticationservice.services.DbQueryService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final DbQueryService dbQueryService;
    public UserDetailsServiceImpl(DbQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = dbQueryService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return UserDetailsImpl.build(user);
    }
}
