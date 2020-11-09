package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import com.kenda.webflux.restful.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("Find By Username")
    void loadUserByUsername() {
        UserDetails details = userService.loadUserByUsername("admin");

        assertNotNull(details);
        assertEquals("admin", details.getUsername());
    }

    @Test
    @DisplayName("Token")
    void token() {
        TokenRequest request = new TokenRequest();
        request.setUsername("admin");
        request.setPassword("admin");

        Mono<User> mono = userService.token(request);

        StepVerifier.create(mono.log())
                .consumeNextWith(user -> assertEquals(request.getUsername(), request.getUsername()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Refresh Token")
    void refreshToken() {
        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjA0OTI3ODExNDk1LCJyb2xlcyI6WyJHVVJVIiwiQURNSU4iXSwidXNlcm5hbWUiOiJhZG1pbiIsInN1YiI6ImFkbWluIiwiaWF0IjoxNjA0OTI3ODExLCJleHAiOjE2MDc4MDc4MTF9.o13qN7roXrqLHWSBiDLCT0YU3uQ7kv0L0kX4U4n0HLH6dAVgsZhygw6l4CLs4ek8VLnVAnF0Z00zcnELyrhsdQ";
        Mono<User> mono = userService.refreshToken(refreshToken);

        StepVerifier.create(mono.log())
                .consumeNextWith(user -> assertEquals("admin", user.getUsername()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Register User")
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("sukenda");
        request.setPassword("sukenda");
        request.setEmail("sukenda@gmail.com");
        request.setProfileName("Profile sukenda");
        request.setRoles(new HashSet<>(Collections.singletonList("ADMIN")));

        Mono<User> mono = userService.signup(request);
        StepVerifier.create(mono.log())
                .consumeNextWith(user -> assertEquals(request.getUsername(), user.getUsername()))
                .verifyComplete();
    }
}