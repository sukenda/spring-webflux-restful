package com.kenda.webflux.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by sukenda
 **/
@Getter
@Setter
@NoArgsConstructor
public class AuthorResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String city;

    private String gender;

}
