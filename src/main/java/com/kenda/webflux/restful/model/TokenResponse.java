package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse implements Serializable {

    private String accessToken;

    private String refreshToken;

    private UserResponse user;

}
