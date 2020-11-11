package com.kenda.webflux.restful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    private String id;

    private String username;

    private String email;

    private String profileName;

    private boolean active;

    private Set<String> roles;

}
