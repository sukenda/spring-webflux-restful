package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.exception.UserException;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import com.kenda.webflux.restful.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = {"classpath:application.properties"})
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    private String refreshToken;

    @Test
    @DisplayName("Register User")
    @Order(1)
    void signup() {
        UserRequest request = new UserRequest();
        request.setUsername("service");
        request.setPassword("service");
        request.setEmail("service@gmail.com");
        request.setProfileName("Profile service");
        request.setRoles(new HashSet<>(Collections.singletonList("ADMIN")));

        Mono<User> mono = userService.signup(request);
        StepVerifier.create(mono.log())
                .consumeNextWith(user -> assertEquals(request.getUsername(), user.getUsername()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Find By Username")
    @Order(2)
    void loadUserByUsername() {
        UserDetails details = userService.loadUserByUsername("service");

        assertNotNull(details);
        assertEquals("service", details.getUsername());
    }

    @Test
    @DisplayName("Token")
    @Order(3)
    void token() {
        TokenRequest request = new TokenRequest();
        request.setUsername("service");
        request.setPassword("service");

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

        Mono<User> mono = userService.refreshToken(refreshToken);

        StepVerifier.create(mono.log())
                .consumeNextWith(user -> assertEquals("service", user.getUsername()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Find By Username Not Found")
    @Order(5)
    void loadUserByUsernameNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.loadUserByUsername("UserTidakAda"));

        String expectedMessage = "User must not be null!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Password wrong")
    @Order(6)
    void passwordWrong() {
        TokenRequest request = new TokenRequest();
        request.setUsername("service");
        request.setPassword("sukenda");

        Mono<User> mono = userService.token(request);

        StepVerifier.create(mono.log())
                .expectError(UserException.class).verify();
    }

}