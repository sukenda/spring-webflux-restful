package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.entity.Comment;
import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.model.*;
import com.kenda.webflux.restful.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private UserService userService;

    private String accessToken;

    private String refreshToken;

    private String postId;

    @Test
    @DisplayName("Register User Post")
    @Order(0)
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("post");
        request.setPassword("post");
        request.setEmail("post@gmail.com");
        request.setProfileName("Profile post");
        request.setRoles(new HashSet<>(Collections.singletonList("ADMIN")));

        webClient.post().uri("/auth/signup")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
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
    @DisplayName("Create Post")
    void create() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul");
        request.setBody("Body");
        request.setComments(Arrays.asList(new Comment("Kenda 1", "Body ke 1"), new Comment("Kenda 2", "Body ke 2")));

        webClient.post().uri("/posts")
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectBody(new ParameterizedTypeReference<RestResponse<PostResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<PostResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    postId = restResponse.getData().getId();
                });

        assertNotNull(postId);
    }

    @Test
    @Order(2)
    @DisplayName("Create Post Not Valid")
    void createNotValid() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul");
        request.setComments(Arrays.asList(new Comment("Kenda 1", "Body ke 1"), new Comment("Kenda 2", "Body ke 2")));

        webClient.post().uri("/posts")
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    @DisplayName("Update Post")
    void update() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul Update");
        request.setBody("Body Update");
        request.setComments(Arrays.asList(new Comment("Kenda 1 Update", "Body ke 1 Update"), new Comment("Kenda 2 Update", "Body ke 2 Update")));

        webClient.put().uri("/posts/{id}", postId)
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<PostResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<PostResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertEquals("Judul Update", restResponse.getData().getTitle());
                    assertEquals("Body Update", restResponse.getData().getBody());
                });
    }

    @Test
    @Order(4)
    @DisplayName("Add Comment Post")
    void comment() {
        assertNotNull(postId);

        Comment request = new Comment("Add Comment", "Body Add Comment");
        webClient.put().uri("/posts/{id}/comment", postId)
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<PostResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<PostResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                });
    }

    @Test
    @Order(5)
    @DisplayName("Update Post Not Found")
    void updateNotFound() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul Update");
        request.setBody("Body Update");
        request.setComments(Arrays.asList(new Comment("Kenda 1 Update", "Body ke 1 Update"), new Comment("Kenda 2 Update", "Body ke 2 Update")));

        webClient.put().uri("/posts/{id}", "notFoundId")
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(6)
    @DisplayName("Update Post Not Valid")
    void updateNotValid() {
        PostRequest request = new PostRequest();
        request.setTitle("Judul Update");
        request.setComments(Arrays.asList(new Comment("Kenda 1 Update", "Body ke 1 Update"), new Comment("Kenda 2 Update", "Body ke 2 Update")));

        webClient.put().uri("/posts/{id}", postId)
                .bodyValue(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(7)
    @DisplayName("Find Post")
    void read() {
        webClient.get().uri("/posts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<List<PostResponse>>>() {
                })
                .consumeWith(response -> {
                    RestResponse<List<PostResponse>> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertNotNull(restResponse.getData());
                });
    }

    @Test
    @Order(8)
    @DisplayName("Find Post by id")
    void findById() {
        webClient.get().uri("/posts/{id}", postId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<PostResponse>>() {
                })
                .consumeWith(response -> {
                    RestResponse<PostResponse> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertNotNull(restResponse.getData());
                });
    }

    @Test
    @Order(9)
    @DisplayName("Delete Token Not Found")
    void tokenNotFound() {
        webClient.delete().uri("/posts/{id}", postId)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @Order(10)
    @DisplayName("Delete Post")
    void delete() {
        webClient.delete().uri("/posts/{id}", postId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RestResponse<String>>() {
                })
                .consumeWith(response -> {
                    RestResponse<String> restResponse = response.getResponseBody();

                    assertNotNull(restResponse);
                    assertEquals(postId, restResponse.getData());
                });
    }

    @Test
    @Order(11)
    @DisplayName("Delete Post Not Found")
    void deleteNotFound() {
        webClient.delete().uri("/posts/{id}", "notFoundId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(12)
    @DisplayName("Delete User Register")
    void deleteUserRegister() {
        Mono<User> mono = userService.delete(refreshToken);

        StepVerifier.create(mono)
                .consumeNextWith(user -> assertEquals(refreshToken, user.getRefreshToken()))
                .expectComplete()
                .verify();
    }
}