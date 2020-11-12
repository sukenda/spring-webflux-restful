package com.kenda.webflux.restful.model;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

    private String id;

    private String username;

    private String email;

    private String profileName;

    private boolean active;

    private Set<String> roles;

}
