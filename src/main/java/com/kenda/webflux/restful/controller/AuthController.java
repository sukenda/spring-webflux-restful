package com.kenda.webflux.restful.controller;

import com.kenda.webflux.restful.model.*;
import com.kenda.webflux.restful.service.UserService;
import com.kenda.webflux.restful.utils.GenericConverter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API description for auth")
public class AuthController {

    private final UserService userService;

    @SecurityRequirements
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<TokenResponse> token(@Valid @RequestBody TokenRequest request) {
        return userService.token(request)
                .flatMap(user -> {
                    UserResponse response = GenericConverter.mapper(user, UserResponse.class);
                    TokenResponse tokenResponse = GenericConverter.mapper(user, TokenResponse.class);
                    tokenResponse.setUser(response);

                    return Mono.just(tokenResponse);
                });
    }

    @SecurityRequirements
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return userService.refreshToken(request.getRefreshToken())
                .flatMap(user -> {
                    UserResponse response = GenericConverter.mapper(user, UserResponse.class);
                    TokenResponse tokenResponse = GenericConverter.mapper(user, TokenResponse.class);
                    tokenResponse.setUser(response);

                    return Mono.just(tokenResponse);
                });
    }

    @SecurityRequirements
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<TokenResponse> signup(@Valid @RequestBody UserRequest request) {
        return userService.signup(request)
                .flatMap(user -> {
                    UserResponse response = GenericConverter.mapper(user, UserResponse.class);
                    TokenResponse tokenResponse = GenericConverter.mapper(user, TokenResponse.class);
                    tokenResponse.setUser(response);

                    return Mono.just(tokenResponse);
                });
    }

}
