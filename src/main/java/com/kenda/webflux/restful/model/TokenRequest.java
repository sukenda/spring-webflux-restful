package com.kenda.webflux.restful.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class TokenRequest implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
