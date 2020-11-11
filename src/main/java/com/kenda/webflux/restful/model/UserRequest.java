package com.kenda.webflux.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class UserRequest implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String profileName;

    @NotEmpty
    private Set<String> roles;

}
