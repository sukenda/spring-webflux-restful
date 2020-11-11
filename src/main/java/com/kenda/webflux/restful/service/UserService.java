package com.kenda.webflux.restful.service;

import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> token(TokenRequest param);

    Mono<User> refreshToken(String refreshToken);

    Mono<User> signup(UserRequest request);

    Mono<User> delete(String refreshToken);

}
