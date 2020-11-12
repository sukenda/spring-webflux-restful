package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.model.RefreshTokenRequest;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.TokenResponse;
import com.kenda.webflux.restful.model.UserRequest;
import com.kenda.webflux.restful.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private UserService userService;

    private String refreshToken;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webClient = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("Register User")
    @Order(1)
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("authController");
        request.setPassword("authController");
        request.setEmail("authController@gmail.com");
        request.setProfileName("Profile authController");
        request.setRoles(new HashSet<>(Arrays.asList("ADMIN", "GURU")));

        webClient.post()
                .uri("/auth/signup")
                .bodyValue(request)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.refreshToken").isNotEmpty()
                .jsonPath("$.user").isNotEmpty()
                .consumeWith(document("Register"));
    }

    @Test
    @DisplayName("Get Token")
    @Order(2)
    void token() {
        TokenRequest request = new TokenRequest();
        request.setUsername("authController");
        request.setPassword("authController");

        webClient.post().uri("/auth/token")
                .bodyValue(request)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.refreshToken").isNotEmpty()
                .jsonPath("$.user").isNotEmpty()
                .consumeWith(document("Token"));
    }

    @Test
    @DisplayName("Service Find Refresh Token")
    @Order(3)
    void findRefreshToken() {
        TokenRequest request = new TokenRequest();
        request.setUsername("authController");
        request.setPassword("authController");

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
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .consumeWith(response -> {
                    TokenResponse restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    refreshToken = restResponse.getRefreshToken();
                }).consumeWith(document("RefreshToken"));
    }

    @Test
    @DisplayName("Register User Exist")
    @Order(5)
    void signupExist() {
        UserRequest request = new UserRequest();
        request.setUsername("authController");
        request.setPassword("authController");
        request.setEmail("authController@gmail.com");
        request.setProfileName("Profile authController");
        request.setRoles(new HashSet<>(Arrays.asList("ADMIN", "GURU")));

        webClient.post().uri("/auth/signup")
                .bodyValue(request)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Token Not Found")
    @Order(6)
    void tokenNotFound() {
        TokenRequest request = new TokenRequest();
        request.setUsername("authController");
        request.setPassword("token");

        webClient.post().uri("/auth/token")
                .bodyValue(request)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    @DisplayName("Refresh Token Not Found")
    @Order(7)
    void refreshTokenNotFound() {
        webClient.post().uri("/auth/refresh-token")
                .bodyValue(new RefreshTokenRequest("refreshTokenNotFound"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();
    }

    @Test
    @Order(8)
    @DisplayName("Delete User Register")
    void deleteUserRegister() {
        Mono<User> mono = userService.delete(refreshToken);

        StepVerifier.create(mono)
                .consumeNextWith(user -> assertEquals(refreshToken, user.getRefreshToken()))
                .expectComplete()
                .verify();
    }

}