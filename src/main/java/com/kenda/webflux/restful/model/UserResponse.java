package com.kenda.webflux.restful.model;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserResponse implements Serializable {

    private String id;

    private String username;

    private String email;

    private String profileName;

    private boolean active;

    private Set<String> roles;

}
