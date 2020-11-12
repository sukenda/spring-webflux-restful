package com.kenda.webflux.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
public class TokenRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
