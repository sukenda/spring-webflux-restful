package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenRequest implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
