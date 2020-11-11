package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;

}
