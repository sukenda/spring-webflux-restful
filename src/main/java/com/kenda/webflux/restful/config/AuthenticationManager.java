package com.kenda.webflux.restful.config;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTTokenProvider tokenProvider;

    public AuthenticationManager(JWTTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            String username = tokenProvider.getUsernameFromToken(authToken);
            if (!tokenProvider.validateToken(authToken)) {
                return Mono.empty();
            }

            Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
            List<String> roles = claims.get(JWTTokenProvider.CLAIM_ROLES, List.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
