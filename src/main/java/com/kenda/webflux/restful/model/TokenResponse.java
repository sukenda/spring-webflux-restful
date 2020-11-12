package com.kenda.webflux.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private UserResponse user;

}
