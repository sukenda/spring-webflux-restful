package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.model.RefreshTokenRequest;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import com.kenda.webflux.restful.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private UserService userService;

    private String refreshToken;

    @Test
    @DisplayName("Register User")
    @Order(1)
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("controller");
        request.setPassword("controller");
        request.setEmail("controller@gmail.com");
        request.setProfileName("Profile controller");
        request.setRoles(new HashSet<>(Arrays.asList("ADMIN", "GURU")));

        webClient.post().uri("/auth/signup")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.refreshToken").isNotEmpty()
                .jsonPath("$.user").isNotEmpty();
    }

    @Test
    @DisplayName("Token")
    @Order(2)
    void token() {
        TokenRequest request = new TokenRequest();
        request.setUsername("controller");
        request.setPassword("controller");

        webClient.post().uri("/auth/token")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.refreshToken").isNotEmpty()
                .jsonPath("$.user").isNotEmpty();
    }

    @Test
    @DisplayName("Service Find Refresh Token")
    @Order(3)
    void findRefreshToken() {
        TokenRequest request = new TokenRequest();
        request.setUsername("controller");
        request.setPassword("controller");

        Mono<User> mono = userService.token(request);

        StepVerifier.create(mono.log())
                .consumeNextWith(user -> refreshToken = user.getRefreshToken()).verifyComplete();

        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("Refresh Token")
    @Order(4)
    void refreshToken() {
        assertNotNull(refreshToken);

        webClient.post().uri("/auth/refresh-token")
                .bodyValue(new RefreshTokenRequest(refreshToken))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.refreshToken").isNotEmpty()
                .jsonPath("$.user").isNotEmpty();
    }

}