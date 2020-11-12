package com.kenda.webflux.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by sukenda
 **/

@Getter
@Setter
@NoArgsConstructor
public class AuthorRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String city;

    @NotBlank
    private String gender;

}
