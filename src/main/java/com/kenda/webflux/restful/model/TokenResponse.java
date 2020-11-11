package com.kenda.webflux.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponse implements Serializable {

    private String accessToken;

    private String refreshToken;

    private UserResponse user;

}
