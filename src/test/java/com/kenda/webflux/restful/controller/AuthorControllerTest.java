package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.model.*;
import com.kenda.webflux.restful.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

/**
 * Created by Kenda on 12 Nov 2020
 * Email soekenda09@gmail.com
 **/
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class AuthorControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private UserService userService;

    private String accessToken;

    private String refreshToken;

    private String authorId;

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
    @DisplayName("Register User Author")
    @Order(0)
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("Author");
        request.setPassword("Author");
        request.setEmail("Author@gmail.com");
        request.setProfileName("Profile Author");
        request.setRoles(new HashSet<>(Collections.singletonList("ADMIN")));

        webClient.post().uri("/auth/signup")
                .bodyValue(request)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .consumeWith(response -> {
                    TokenResponse tokenResponse = response.getResponseBody();

                    assertNotNull(tokenResponse);
                    accessToken = tokenResponse.getAccessToken();
                    refreshToken = tokenResponse.getRefreshToken();
                });
    }

    @Test
    @Order(1)
    @DisplayName("Create Author")
    void save() {
        AuthorRequest request = new AuthorRequest();
        request.setFirstName("Kenda");
        request.setLastName("Sukenda");
        request.setCity("Cirebon");
        request.setGender("Male");

        webClient.post().uri("/authors")
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectBody(new ParameterizedTypeReference<RestResponse<AuthorResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<AuthorResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    authorId = restResponse.getData().getId();
                });

        assertNotNull(authorId);
    }

    @Test
    @Order(2)
    @DisplayName("Create Author Not Valid")
    void saveNotValid() {
        AuthorRequest request = new AuthorRequest();
        request.setLastName("Sukenda");
        request.setCity("Cirebon");
        request.setGender("Male");

        webClient.post().uri("/authors")
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    @DisplayName("Update Author")
    void update() {
        AuthorRequest request = new AuthorRequest();
        request.setFirstName("Kenda Update");
        request.setLastName("Sukenda Update");
        request.setCity("Cirebon Update");
        request.setGender("Male");

        webClient.put().uri("/authors/{id}", authorId)
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<AuthorResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<AuthorResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertEquals("Kenda Update", restResponse.getData().getFirstName());
                    assertEquals("Sukenda Update", restResponse.getData().getLastName());
                });
    }

    @Test
    @Order(4)
    @DisplayName("Update Author Not Found")
    void updateNotFound() {
        AuthorRequest request = new AuthorRequest();
        request.setLastName("Sukenda");
        request.setCity("Cirebon");
        request.setGender("Male");

        webClient.put().uri("/authors/{id}", "notFoundId")
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(5)
    @DisplayName("Update Author Not Valid")
    void updateNotValid() {
        AuthorRequest request = new AuthorRequest();
        request.setLastName("Sukenda");
        request.setCity("Cirebon");
        request.setGender("Male");

        webClient.put().uri("/authors/{id}", authorId)
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(6)
    @DisplayName("Find Author by id")
    void findById() {
        webClient.get().uri("/authors/{id}", authorId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<AuthorResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<AuthorResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertNotNull(restResponse.getData());
                });
    }

    @Test
    @Order(7)
    @DisplayName("Find Author by page")
    void find() {
        webClient.get().uri(uriBuilder ->
                uriBuilder
                        .path("/authors")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<List<AuthorResponse>>>() {
                })
                .consumeWith(response -> {
                    RestResponse<List<AuthorResponse>> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertNotNull(restResponse.getData());
                });
    }

    @Test
    @Order(9)
    @DisplayName("Delete Author")
    void delete() {
        webClient.delete().uri("/authors/{id}", authorId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<String>>() {
                })
                .consumeWith(response -> {
                    RestResponse<String> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertEquals(authorId, restResponse.getData());
                });
    }

    @Test
    @Order(9)
    @DisplayName("Delete User Register")
    void deleteUserRegister() {
        Mono<User> mono = userService.delete(refreshToken);

        StepVerifier.create(mono)
                .consumeNextWith(user -> assertEquals(refreshToken, user.getRefreshToken()))
                .expectComplete()
                .verify();
    }
}