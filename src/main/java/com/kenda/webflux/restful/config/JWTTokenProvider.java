package com.kenda.webflux.restful.config;

import com.kenda.webflux.restful.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.security.Key;
import java.util.*;


@Component
@Slf4j
public class JWTTokenProvider implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String CLAIM_CREATED = "created";
    private static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLES = "roles";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String expirationTime;

    @Value("${jwt.refresh.expiration}")
    private String expirationRefreshTime;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User param, Set<String> roles, boolean refresh) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USERNAME, param.getUsername());
        claims.put(CLAIM_ROLES, roles);
        claims.put(CLAIM_CREATED, new Date());

        long expirationTimeLong = refresh ? Long.parseLong(expirationRefreshTime) : Long.parseLong(expirationTime); //in second
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(param.getUsername())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
