package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;

}
