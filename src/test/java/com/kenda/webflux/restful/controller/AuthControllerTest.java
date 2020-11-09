package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.model.RefreshTokenRequest;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("Token")
    void token() {
        TokenRequest request = new TokenRequest();
        request.setUsername("admin");
        request.setPassword("admin");

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
    @DisplayName("Refresh Token")
    void refreshToken() {
        webClient.post().uri("/auth/refresh-token")
                .bodyValue(new RefreshTokenRequest("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjA0OTI3MTM0MDU4LCJyb2xlcyI6WyJHVVJVIiwiQURNSU4iXSwidXNlcm5hbWUiOiJhZG1pbiIsInN1YiI6ImFkbWluIiwiaWF0IjoxNjA0OTI3MTM0LCJleHAiOjE2MDc4MDcxMzR9.237kw2hOKHJ6H9o8PkrUn_uZ-qzB5BF_O3dzW3jHiL-QF--NGy_lSnbecdVelzKSfHtQes5kiOnLVzHen1_Rtw"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.refreshToken").isNotEmpty()
                .jsonPath("$.user").isNotEmpty();
    }

    @Test
    @DisplayName("Register User")
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        request.setPassword("admin");
        request.setEmail("admin@gmail.com");
        request.setProfileName("Profile admin");
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
}